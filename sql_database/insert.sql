insert into Tag VALUES ('untag', 'description dun tag');
INSERT INTO typecapteur(
            idtype, description)
    VALUES ('temp','temperature');
INSERT INTO board(
            idboard, nom, description)
    VALUES ('ard-101','mon arduino','arduino number 1');
INSERT INTO bridge(
            idbridge, ip, port, nom, loc)
    VALUES ('pi-1','home.cecchinel.fr','9001','pi 1','appartement de cyril');
INSERT INTO capteur(
            idcapteur, nom, description, frequency, type)
    VALUES ('ctempcyr','capt-temp-cyril','capteur de temperature chez cyril',5,'temp'),
           ('ctempmat','capt-temp-mat','capteur de temperature chez matthieu',10,'temp');
INSERT INTO capteurphy(
            idcapteur, timestart, timestop, boardpin, endpointIP, port, idboard)
    VALUES ('ctempcyr', '6:0', '16:0', '8', 'home.dimecco.fr',5001, 'ard-101'),
           ('ctempmat', '6:0', '16:0', '8', 'home.dimecco.fr',5001, 'ard-101')
;
--INSERT INTO capteurvirt(
  --          idcapteur, script)
    --VALUES (?, ?);
--INSERT INTO correspondt(
  --          idcapteur, name)
    --VALUES (?, ?);
--INSERT INTO tag(
  --          name, description)
    --VALUES (?, ?);

