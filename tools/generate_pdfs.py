#!/usr/bin/env python3
"""Convierte los .md de docs/ a PDF reales usando reportlab.
Soporta: encabezados (#, ##, ###), listas con '-', tablas markdown simples,
negritas **texto**, bloques de código y párrafos normales. Pensado para
los tres documentos de VerdeLegal (memoria, manual de usuario, manual
técnico) -- no es un parser de markdown genérico.
"""
import re
import sys
from reportlab.lib.pagesizes import LETTER
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import cm
from reportlab.lib import colors
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, ListFlowable,
    ListItem, PageBreak, Preformatted
)
from reportlab.lib.enums import TA_LEFT

FOREST_DEEP = colors.HexColor("#1B4332")
FOREST_MID = colors.HexColor("#2D6A4F")
RIVER_BLUE = colors.HexColor("#1D8FA6")
INK = colors.HexColor("#243B34")


def build_styles():
    styles = getSampleStyleSheet()
    styles.add(ParagraphStyle(name="VLTitle", parent=styles["Title"], textColor=FOREST_DEEP, spaceAfter=18))
    styles.add(ParagraphStyle(name="VLH1", parent=styles["Heading1"], textColor=FOREST_DEEP, spaceBefore=16, spaceAfter=8))
    styles.add(ParagraphStyle(name="VLH2", parent=styles["Heading2"], textColor=FOREST_MID, spaceBefore=12, spaceAfter=6))
    styles.add(ParagraphStyle(name="VLH3", parent=styles["Heading3"], textColor=RIVER_BLUE, spaceBefore=10, spaceAfter=4))
    styles.add(ParagraphStyle(name="VLBody", parent=styles["BodyText"], textColor=INK, alignment=TA_LEFT, spaceAfter=8, leading=15))
    styles.add(ParagraphStyle(name="VLBullet", parent=styles["BodyText"], textColor=INK, leading=14))
    styles.add(ParagraphStyle(name="VLCode", parent=styles["Code"], fontSize=8, leading=10, backColor=colors.HexColor("#F0F0F0")))
    return styles


def inline_md(text):
    text = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
    text = re.sub(r"\*\*(.+?)\*\*", r"<b>\1</b>", text)
    text = re.sub(r"`([^`]+)`", r"<font face='Courier'>\1</font>", text)
    return text


def parse_table(lines, start):
    rows = []
    i = start
    while i < len(lines) and lines[i].strip().startswith("|"):
        row = [c.strip() for c in lines[i].strip().strip("|").split("|")]
        if not re.match(r"^:?-+:?$", row[0].replace(" ", "")):
            rows.append(row)
        i += 1
    return rows, i


def md_to_flowables(md_text, styles):
    flow = []
    lines = md_text.split("\n")
    i = 0
    in_code = False
    code_buf = []
    bullet_buf = []

    def flush_bullets():
        nonlocal bullet_buf
        if bullet_buf:
            items = [ListItem(Paragraph(inline_md(b), styles["VLBullet"])) for b in bullet_buf]
            flow.append(ListFlowable(items, bulletType="bullet", start="circle", leftIndent=16))
            flow.append(Spacer(1, 6))
            bullet_buf = []

    def is_special(s):
        return (
            not s or s.startswith("```") or s.startswith("> ") or s.startswith("|")
            or s.startswith("#### ") or s.startswith("### ") or s.startswith("## ") or s.startswith("# ")
            or s.startswith("====") or s.startswith("----")
            or s.startswith("- ") or s.startswith("* ") or re.match(r"^\d+\.\s", s)
        )

    while i < len(lines):
        line = lines[i]
        stripped = line.strip()

        if stripped.startswith("```"):
            if not in_code:
                in_code = True
                code_buf = []
            else:
                in_code = False
                flow.append(Preformatted("\n".join(code_buf), styles["VLCode"]))
                flow.append(Spacer(1, 8))
            i += 1
            continue
        if in_code:
            code_buf.append(line)
            i += 1
            continue

        if not stripped:
            flush_bullets()
            i += 1
            continue

        if stripped.startswith("> "):
            flush_bullets()
            flow.append(Paragraph("<i>" + inline_md(stripped[2:]) + "</i>", styles["VLBody"]))
            i += 1
            continue

        if stripped.startswith("|"):
            flush_bullets()
            rows, i = parse_table(lines, i)
            if rows:
                wrapped = [[Paragraph(inline_md(c), styles["VLBullet"]) for c in r] for r in rows]
                t = Table(wrapped, hAlign="LEFT")
                t.setStyle(TableStyle([
                    ("BACKGROUND", (0, 0), (-1, 0), FOREST_MID),
                    ("TEXTCOLOR", (0, 0), (-1, 0), colors.white),
                    ("FONTNAME", (0, 0), (-1, 0), "Helvetica-Bold"),
                    ("GRID", (0, 0), (-1, -1), 0.5, colors.HexColor("#CCCCCC")),
                    ("VALIGN", (0, 0), (-1, -1), "TOP"),
                    ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.white, colors.HexColor("#F7F5EE")]),
                    ("LEFTPADDING", (0, 0), (-1, -1), 5),
                    ("RIGHTPADDING", (0, 0), (-1, -1), 5),
                    ("TOPPADDING", (0, 0), (-1, -1), 4),
                    ("BOTTOMPADDING", (0, 0), (-1, -1), 4),
                ]))
                flow.append(t)
                flow.append(Spacer(1, 10))
            continue

        if stripped.startswith("#### "):
            flush_bullets(); flow.append(Paragraph(inline_md(stripped[5:]), styles["VLH3"])); i += 1; continue
        if stripped.startswith("### "):
            flush_bullets(); flow.append(Paragraph(inline_md(stripped[4:]), styles["VLH3"])); i += 1; continue
        if stripped.startswith("## "):
            flush_bullets(); flow.append(Paragraph(inline_md(stripped[3:]), styles["VLH2"])); i += 1; continue
        if stripped.startswith("# "):
            flush_bullets(); flow.append(Paragraph(inline_md(stripped[2:]), styles["VLTitle"])); i += 1; continue

        if stripped.startswith("====") or stripped.startswith("----"):
            i += 1
            continue

        if stripped.startswith("- ") or stripped.startswith("* "):
            # Une continuaciones de la misma viñeta (líneas siguientes que no son especiales).
            buf = stripped[2:]
            i += 1
            while i < len(lines) and lines[i].strip() and not is_special(lines[i].strip()):
                buf += " " + lines[i].strip()
                i += 1
            bullet_buf.append(buf)
            continue

        if re.match(r"^\d+\.\s", stripped):
            buf = re.sub(r"^\d+\.\s", "", stripped)
            i += 1
            while i < len(lines) and lines[i].strip() and not is_special(lines[i].strip()):
                buf += " " + lines[i].strip()
                i += 1
            bullet_buf.append(buf)
            continue

        # Párrafo normal: unir líneas consecutivas no especiales en un solo bloque
        # antes de aplicar markdown en línea, para que **negritas** que cruzan un
        # salto de línea "duro" del .md se conviertan correctamente.
        flush_bullets()
        buf = stripped
        i += 1
        while i < len(lines) and lines[i].strip() and not is_special(lines[i].strip()):
            buf += " " + lines[i].strip()
            i += 1
        flow.append(Paragraph(inline_md(buf), styles["VLBody"]))

    flush_bullets()
    return flow


def convert(md_path, pdf_path, title):
    with open(md_path, "r", encoding="utf-8") as f:
        content = f.read()

    styles = build_styles()
    doc = SimpleDocTemplate(
        pdf_path, pagesize=LETTER,
        topMargin=2.2 * cm, bottomMargin=2.2 * cm, leftMargin=2.2 * cm, rightMargin=2.2 * cm,
        title=title, author="VerdeLegal"
    )
    flow = md_to_flowables(content, styles)
    doc.build(flow)
    print(f"OK: {pdf_path}")


if __name__ == "__main__":
    jobs = [
        ("docs/MEMORIA_DESCRIPTIVA.md", "docs/pdf/MEMORIA_DESCRIPTIVA.pdf", "VerdeLegal - Memoria Descriptiva"),
        ("docs/MANUAL_USUARIO.md", "docs/pdf/MANUAL_USUARIO.pdf", "VerdeLegal - Manual de Usuario"),
        ("docs/MANUAL_TECNICO.md", "docs/pdf/MANUAL_TECNICO.pdf", "VerdeLegal - Manual Tecnico"),
    ]
    for md_path, pdf_path, title in jobs:
        convert(md_path, pdf_path, title)
