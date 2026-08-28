-- VerdeLegal — schema.sql
-- Referencia en SQL puro del esquema Room real (app/src/main/java/.../data/local).
-- SQLite (motor usado por Room en Android).

PRAGMA foreign_keys = ON;

CREATE TABLE user_profile (
    id                    INTEGER PRIMARY KEY AUTOINCREMENT,
    alias                 TEXT NOT NULL,
    avatarKey             TEXT NOT NULL,
    createdAt             INTEGER NOT NULL,
    soundEnabled          INTEGER NOT NULL DEFAULT 1,
    hapticEnabled         INTEGER NOT NULL DEFAULT 1,
    onboardingCompleted   INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE environmental_zone (
    id                     INTEGER PRIMARY KEY AUTOINCREMENT,
    code                   TEXT NOT NULL,
    displayName            TEXT NOT NULL,
    shortDescription       TEXT NOT NULL,
    mapOrder               INTEGER NOT NULL,
    mapX                   REAL NOT NULL,
    mapY                   REAL NOT NULL,
    unlockRequiredBadges   INTEGER NOT NULL,
    iconKey                TEXT NOT NULL
);

CREATE TABLE environmental_scenario (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    zoneId        INTEGER NOT NULL,
    title         TEXT NOT NULL,
    lumaIntro     TEXT NOT NULL,
    sceneOrder    INTEGER NOT NULL,
    backgroundKey TEXT NOT NULL,
    FOREIGN KEY (zoneId) REFERENCES environmental_zone(id) ON DELETE CASCADE
);
CREATE INDEX idx_scenario_zone ON environmental_scenario(zoneId);

CREATE TABLE environmental_issue (
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    scenarioId   INTEGER NOT NULL,
    title        TEXT NOT NULL,
    description  TEXT NOT NULL,
    iconKey      TEXT NOT NULL,
    severity     TEXT NOT NULL,
    positionX    REAL NOT NULL,
    positionY    REAL NOT NULL,
    FOREIGN KEY (scenarioId) REFERENCES environmental_scenario(id) ON DELETE CASCADE
);
CREATE INDEX idx_issue_scenario ON environmental_issue(scenarioId);

CREATE TABLE challenge (
    id             INTEGER PRIMARY KEY AUTOINCREMENT,
    scenarioId     INTEGER NOT NULL,
    type           TEXT NOT NULL,
    title          TEXT NOT NULL,
    prompt         TEXT NOT NULL,
    difficulty     INTEGER NOT NULL,
    xpReward       INTEGER NOT NULL,
    challengeOrder INTEGER NOT NULL,
    FOREIGN KEY (scenarioId) REFERENCES environmental_scenario(id) ON DELETE CASCADE
);
CREATE INDEX idx_challenge_scenario ON challenge(scenarioId);

CREATE TABLE decision (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    challengeId   INTEGER NOT NULL,
    text          TEXT NOT NULL,
    isCorrect     INTEGER NOT NULL,
    isPartial     INTEGER NOT NULL,
    decisionOrder INTEGER NOT NULL,
    FOREIGN KEY (challengeId) REFERENCES challenge(id) ON DELETE CASCADE
);
CREATE INDEX idx_decision_challenge ON decision(challengeId);

CREATE TABLE consequence (
    id                          INTEGER PRIMARY KEY AUTOINCREMENT,
    description                 TEXT NOT NULL,
    severity                    TEXT NOT NULL,
    visualKey                   TEXT NOT NULL,
    relatedRestorationMissionId INTEGER
);

CREATE TABLE decision_outcome (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    decisionId      INTEGER NOT NULL,
    explanationText TEXT NOT NULL,
    consequenceId   INTEGER,
    FOREIGN KEY (decisionId) REFERENCES decision(id) ON DELETE CASCADE,
    FOREIGN KEY (consequenceId) REFERENCES consequence(id) ON DELETE SET NULL
);
CREATE INDEX idx_outcome_decision ON decision_outcome(decisionId);
CREATE INDEX idx_outcome_consequence ON decision_outcome(consequenceId);

CREATE TABLE restoration_mission (
    id                INTEGER PRIMARY KEY AUTOINCREMENT,
    zoneId            INTEGER NOT NULL,
    title             TEXT NOT NULL,
    description       TEXT NOT NULL,
    xpReward          INTEGER NOT NULL,
    badgeIdOnComplete INTEGER,
    FOREIGN KEY (zoneId) REFERENCES environmental_zone(id) ON DELETE CASCADE
);
CREATE INDEX idx_mission_zone ON restoration_mission(zoneId);

CREATE TABLE restoration_step (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    missionId     INTEGER NOT NULL,
    description   TEXT NOT NULL,
    stepOrder     INTEGER NOT NULL,
    itemKey       TEXT NOT NULL,
    targetSlotKey TEXT NOT NULL,
    FOREIGN KEY (missionId) REFERENCES restoration_mission(id) ON DELETE CASCADE
);
CREATE INDEX idx_step_mission ON restoration_step(missionId);

CREATE TABLE authorization_activity (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    zoneId        INTEGER NOT NULL,
    activityName  TEXT NOT NULL,
    description   TEXT NOT NULL,
    iconKey       TEXT NOT NULL,
    correctChoice TEXT NOT NULL,
    FOREIGN KEY (zoneId) REFERENCES environmental_zone(id) ON DELETE CASCADE
);
CREATE INDEX idx_authactivity_zone ON authorization_activity(zoneId);

CREATE TABLE environmental_impact (
    id                       INTEGER PRIMARY KEY AUTOINCREMENT,
    authorizationActivityId  INTEGER NOT NULL,
    impactText               TEXT NOT NULL,
    impactLevel              TEXT NOT NULL,
    FOREIGN KEY (authorizationActivityId) REFERENCES authorization_activity(id) ON DELETE CASCADE
);
CREATE INDEX idx_impact_activity ON environmental_impact(authorizationActivityId);

CREATE TABLE protection_measure (
    id                       INTEGER PRIMARY KEY AUTOINCREMENT,
    authorizationActivityId  INTEGER NOT NULL,
    measureText              TEXT NOT NULL,
    isRecommended            INTEGER NOT NULL,
    FOREIGN KEY (authorizationActivityId) REFERENCES authorization_activity(id) ON DELETE CASCADE
);
CREATE INDEX idx_measure_activity ON protection_measure(authorizationActivityId);

CREATE TABLE authorization_decision (
    id                       INTEGER PRIMARY KEY AUTOINCREMENT,
    authorizationActivityId  INTEGER NOT NULL,
    userId                   INTEGER NOT NULL,
    choice                   TEXT NOT NULL,
    isCorrect                INTEGER NOT NULL,
    timestamp                INTEGER NOT NULL,
    FOREIGN KEY (authorizationActivityId) REFERENCES authorization_activity(id) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES user_profile(id) ON DELETE CASCADE
);
CREATE INDEX idx_authdecision_activity ON authorization_decision(authorizationActivityId);
CREATE INDEX idx_authdecision_user ON authorization_decision(userId);

CREATE TABLE challenge_attempt (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    challengeId   INTEGER NOT NULL,
    userId        INTEGER NOT NULL,
    success       INTEGER NOT NULL,
    partial       INTEGER NOT NULL,
    attemptNumber INTEGER NOT NULL,
    timestamp     INTEGER NOT NULL,
    FOREIGN KEY (challengeId) REFERENCES challenge(id) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES user_profile(id) ON DELETE CASCADE
);
CREATE INDEX idx_attempt_challenge ON challenge_attempt(challengeId);
CREATE INDEX idx_attempt_user ON challenge_attempt(userId);

CREATE TABLE progress (
    id                        INTEGER PRIMARY KEY AUTOINCREMENT,
    userId                    INTEGER NOT NULL,
    zoneId                    INTEGER NOT NULL,
    challengesCompleted       INTEGER NOT NULL DEFAULT 0,
    totalChallenges           INTEGER NOT NULL DEFAULT 0,
    restorationCompleted      INTEGER NOT NULL DEFAULT 0,
    totalRestorationMissions  INTEGER NOT NULL DEFAULT 0,
    authorizationsCorrect     INTEGER NOT NULL DEFAULT 0,
    totalAuthorizations       INTEGER NOT NULL DEFAULT 0,
    xp                        INTEGER NOT NULL DEFAULT 0,
    status                    TEXT NOT NULL DEFAULT 'LOCKED',
    FOREIGN KEY (userId) REFERENCES user_profile(id) ON DELETE CASCADE,
    FOREIGN KEY (zoneId) REFERENCES environmental_zone(id) ON DELETE CASCADE
);
CREATE INDEX idx_progress_user ON progress(userId);
CREATE INDEX idx_progress_zone ON progress(zoneId);
CREATE UNIQUE INDEX idx_progress_user_zone ON progress(userId, zoneId);

CREATE TABLE badge (
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    code         TEXT NOT NULL,
    name         TEXT NOT NULL,
    description  TEXT NOT NULL,
    iconKey      TEXT NOT NULL,
    criteriaKey  TEXT NOT NULL
);

CREATE TABLE user_badge (
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    userId   INTEGER NOT NULL,
    badgeId  INTEGER NOT NULL,
    earnedAt INTEGER NOT NULL,
    FOREIGN KEY (userId) REFERENCES user_profile(id) ON DELETE CASCADE,
    FOREIGN KEY (badgeId) REFERENCES badge(id) ON DELETE CASCADE
);
CREATE INDEX idx_userbadge_user ON user_badge(userId);
CREATE INDEX idx_userbadge_badge ON user_badge(badgeId);
CREATE UNIQUE INDEX idx_userbadge_user_badge ON user_badge(userId, badgeId);

CREATE TABLE unlocked_zone (
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    userId     INTEGER NOT NULL,
    zoneId     INTEGER NOT NULL,
    unlockedAt INTEGER NOT NULL,
    FOREIGN KEY (userId) REFERENCES user_profile(id) ON DELETE CASCADE,
    FOREIGN KEY (zoneId) REFERENCES environmental_zone(id) ON DELETE CASCADE
);
CREATE INDEX idx_unlockedzone_user ON unlocked_zone(userId);
CREATE INDEX idx_unlockedzone_zone ON unlocked_zone(zoneId);
CREATE UNIQUE INDEX idx_unlockedzone_user_zone ON unlocked_zone(userId, zoneId);
