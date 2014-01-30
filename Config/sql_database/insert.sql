insert into Tag VALUES ('untag', 'description dun tag');
INSERT INTO typecapteur(
            idtype, description)
    VALUES ('temp','temperature');
INSERT INTO board(
            idboard, nom, description)
    VALUES ('NEWTON_B332_B2','mon arduino 2','arduino number 2'),
           ('NEWTON_B332_B1','mon arduino 1','arduino number 1');

INSERT INTO bridge(
            idbridge, ip, port, nom, loc)
    VALUES ('pi-1','home.cecchinel.fr','9001','pi 1','appartement de cyril'),
           ('pi-2','192.168.1.102','9001','pi 2','appartement de cyril'),
           ('pi-3','192.168.1.48','9001','pi 3','appartement de cyril');

INSERT INTO capteur(
            idcapteur, nom, description, frequency, type)
    VALUES ('ctempcyr','capt-temp-cyril','capteur de temperature chez cyril',5,'temp'),
           ('ctempmat','capt-temp-mat','capteur de temperature chez matthieu',10,'temp'),
           ('ctempcyr2','capt-temp-cyril','capteur de temperature chez cyril',5,'temp'),
           ('ctempmat2','capt-temp-mat','capteur de temperature chez matthieu',10,'temp');

INSERT INTO capteurphy(
            idcapteur, timestart, timestop, boardpin, endpointIP, port, idboard)
    VALUES ('ctempcyr2', '6:0', '16:0', '8', 'home.dimecco.fr',5001, 'NEWTON_B332_B2'),
           ('ctempmat2', '6:0', '16:0', '8', 'home.dimecco.fr',5001, 'NEWTON_B332_B2'),
           ('ctempcyr', '6:0', '16:0', '8', 'home.dimecco.fr',5001, 'NEWTON_B332_B1'),
           ('ctempmat', '6:0', '16:0', '8', 'home.dimecco.fr',5001, 'NEWTON_B332_B1');

--INSERT INTO capteurvirt(
  --          idcapteur, script)
    --VALUES (?, ?);
--INSERT INTO correspondt(
  --          idcapteur, name)
    --VALUES (?, ?);
--INSERT INTO tag(
  --          name, description)
    --VALUES (?, ?);

