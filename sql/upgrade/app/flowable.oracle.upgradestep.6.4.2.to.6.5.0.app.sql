

UPDATE ACT_APP_DATABASECHANGELOGLOCK SET LOCKED = 1, LOCKEDBY = '192.168.10.1 (192.168.10.1)', LOCKGRANTED = to_timestamp('2020-01-22 07:30:30.423', 'YYYY-MM-DD HH24:MI:SS.FF') WHERE ID = 1 AND LOCKED = 0;

UPDATE ACT_APP_DATABASECHANGELOGLOCK SET LOCKED = 0, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;

