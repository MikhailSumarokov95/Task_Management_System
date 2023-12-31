DROP TABLE IF EXISTS
users,
task,
comment
CASCADE;

CREATE TABLE IF NOT EXISTS users(
    id          BIGSERIAL PRIMARY KEY    NOT NULL,
    email       VARCHAR(64)              NOT NULL,
    password    VARCHAR(64)              NOT NULL,
    CONSTRAINT email_unique UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS task(
    id             BIGSERIAL PRIMARY KEY    NOT NULL,
    title          VARCHAR(64)              NOT NULL,
    description    VARCHAR(1024)            NOT NULL,
    status         VARCHAR(32)              NOT NULL,
    priority       VARCHAR(32)              NOT NULL,
    author_id      BIGSERIAL                NOT NULL
                                            REFERENCES users(id) ON DELETE CASCADE,
    executor_id    BIGSERIAL                REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS comment(
    id           BIGSERIAL PRIMARY KEY    NOT NULL,
    content      VARCHAR(1024)            NOT NULL,
    author_id    BIGSERIAL                NOT NULL
                                          REFERENCES users(id) ON DELETE CASCADE,
    task_id      BIGSERIAL                NOT NULL
                                          REFERENCES task(id) ON DELETE CASCADE
);