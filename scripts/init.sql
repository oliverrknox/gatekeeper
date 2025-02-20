CREATE USER username WITH PASSWORD 'password';

GRANT CREATE ON SCHEMA public TO username;
GRANT USAGE ON SCHEMA public TO username;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO username;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO username;
