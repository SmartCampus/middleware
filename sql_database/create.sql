drop table CorrespondT;
drop table Tag;
drop table CapteurPhy;
drop table CapteurVirt;
drop table Capteur;
drop table Bridge;
drop table TypeCapteur;
drop table Board;

create table Bridge(
		idBridge VARCHAR(16),
		ip VARCHAR(32),
		port int4,
		nom VARCHAR(32),
		loc VARCHAR(32),
		PRIMARY KEY (idBridge)
);
CREATE TABLE Board (
		idBoard VARCHAR(16),
		nom VARCHAR(16),
		description VARCHAR (32),
		PRIMARY KEY (idBoard)
);

CREATE TABLE TypeCapteur (
  idtype VARCHAR(16),
  description VARCHAR(16),
  PRIMARY KEY (idtype)
);

CREATE TABLE Capteur (
		idCapteur VARCHAR(16),
		nom VARCHAR(32),
    description text,
    frequency int4,
		type VARCHAR(16) NOT NULL REFERENCES TypeCapteur,
		PRIMARY KEY (idCapteur)
);

CREATE TABLE CapteurPhy (
		idCapteur VARCHAR(16) NOT NULL REFERENCES Capteur,
		timeStart time,
    timeStop time,
    boardPin VARCHAR (8),
    endPointIP VARCHAR (32),
    port int4,
    idBoard VARCHAR (16) REFERENCES Board,
		PRIMARY KEY (idCapteur)
);
CREATE TABLE CapteurVirt (
    idCapteur VARCHAR(16) NOT NULL REFERENCES Capteur,
    script TEXT,
    PRIMARY KEY (idCapteur)
);

CREATE TABLE Tag (
    name VARCHAR (16),
    description text,
    PRIMARY KEY (name)
);
CREATE TABLE CorrespondT (
		idCapteur VARCHAR(16) NOT NULL REFERENCES Capteur,
		name VARCHAR (16) NOT NULL REFERENCES Tag,
		PRIMARY KEY (idCapteur,name)
);

