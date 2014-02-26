insert into Tag VALUES ('untag', 'description dun tag');
INSERT INTO typecapteur(
            idtype, description)
    VALUES ('temp','temperature'),
           ('push','pression');
INSERT INTO board(
            idboard, nom, description)
    VALUES ('SMART_CAMPUS_BOARD_1','mon arduino 3','arduino number 2'),
           ('SMART_CAMPUS_BOARD_2','mon arduino 2','arduino number 2'),
           ('SMART_CAMPUS_BOARD_3','mon arduino 1','arduino number 1');


INSERT INTO bridge(
            idbridge, ip, port, nom, loc)
    VALUES ('pi-1','home.cecchinel.fr','9001','pi 1','appartement de cyril'),
           ('pi-2','home.cecchinel.fr','9002','pi 2','appartement de cyril');



INSERT INTO capteur(
            idcapteur, nom, description, frequency, type)
    VALUES ('TEMP_SENSOR_B2','capt-temp-cyril','capteur de temperature chez cyril',1,'temp'),
           ('PUSH_SENSOR_B2','capt-temp-mat','capteur de temperature chez matthieu',1,'push'),
           ('TEMP_SENSOR_B3','capt-temp-cyril','capteur de temperature chez cyril',1,'temp'),
           ('TEMP_SENSOR_B1','capt-temp-mat','capteur de temperature chez matthieu',1,'temp');

INSERT INTO capteurphy(
            idcapteur, timestart, timestop, boardpin, endpointIP, port, idboard)
    VALUES ('TEMP_SENSOR_B2', '6:0', '16:0', '3', '192.168.1.200',8080, 'SMART_CAMPUS_BOARD_2'),
           ('PUSH_SENSOR_B2', '6:0', '16:0', '2', '192.168.1.200',8080, 'SMART_CAMPUS_BOARD_2'),
           ('TEMP_SENSOR_B1', '6:0', '16:0', '1', '192.168.1.200',8080, 'SMART_CAMPUS_BOARD_3'),
           ('TEMP_SENSOR_B3', '6:0', '16:0', '1', '192.168.1.200',8080, 'SMART_CAMPUS_BOARD_1');

--INSERT INTO capteurvirt(
  --          idcapteur, script)
    --VALUES (?, ?);
--INSERT INTO correspondt(
  --          idcapteur, name)
    --VALUES (?, ?);
--INSERT INTO tag(
  --          name, description)
    --VALUES (?, ?);

