DROP TABLE IF EXISTS "inventario";
CREATE TABLE inventario(
idInventario INTEGER PRIMARY KEY,
manos1 int,
manos2 int,
pie int,
cabeza int,
pecho int,
accesorio int
);
INSERT INTO "inventario" VALUES(1,-1,-1,-1,-1,-1,-1);
DROP TABLE IF EXISTS "item";
CREATE TABLE item(
idItem INTEGER PRIMARY KEY AUTOINCREMENT,
bonoAtaque int,
bonoDefensa int,
BonoMagia int,
bonoSalud int,
bonoEnergia int,
fuerzaRequerida int,
destrezaRequerida int,
inteligenciaRequerida int
, "Nombe" VARCHAR);
INSERT INTO "item" VALUES(1,0,0,0,15,0,0,0,0,'Pocion de Salud');
INSERT INTO "item" VALUES(2,0,0,0,0,20,0,0,0,'Pocion de energia');
INSERT INTO "item" VALUES(3,0,100,0,0,0,0,0,0,'Escudo');
INSERT INTO "item" VALUES(4,0,0,0,0,0,50,0,0,'Armamento');
INSERT INTO "item" VALUES(5,0,0,0,0,0,0,30,0,'Agua');
INSERT INTO "item" VALUES(6,0,0,0,0,0,0,0,10,'Libros');
INSERT INTO "item" VALUES(7,0,0,100,0,0,0,0,0,'Varita Magica');
INSERT INTO "item" VALUES(8,0,0,0,-10,0,0,0,0,'Veneno');
INSERT INTO "item" VALUES(9,0,0,0,0,-50,0,0,0,'Pocion de cansancio');
INSERT INTO "item" VALUES(10,0,-30,0,0,0,0,0,0,'Robo de armamento');
INSERT INTO "item" VALUES(11,0,-30,0,0,0,0,-35,0,'Lenditud');
DROP TABLE IF EXISTS "itemModificado";
CREATE TABLE itemModificado(
idItemModificado INTEGER PRIMARY KEY AUTOINCREMENT,
bonoAtaque int,
bonoDefensa int,
BonoMagia int,
bonoSalud int,
bonoEnergia int,
fuerzaRequerida int,
destrezaRequerida int,
inteligenciaRequerida int
);
DROP TABLE IF EXISTS "mochila";
CREATE TABLE "mochila" ("idMochila" INTEGER PRIMARY KEY ,"item1" int,"item2" int,"item3" int,"item4" int,"item5" int,"item6" int,"item7" int,"item8" int,"item9" int,"item10" int,"item11" int);
INSERT INTO "mochila" VALUES(1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1);
DROP TABLE IF EXISTS "personaje";
CREATE TABLE personaje(
idPersonaje INTEGER PRIMARY KEY AUTOINCREMENT,
idInventario int,
idMochila int,
casta varchar(15),
raza varchar (15),
fuerza int,
destreza int,
inteligencia int,
saludTope int,
energiaTope int,
nombre varchar (15),
experiencia int,
nivel int,
idAlianza int
);
INSERT INTO "personaje" VALUES(1,1,1,'Guerrero','Humano',15,10,10,55,55,'NicoSOAD',0,1,-1);
DROP TABLE IF EXISTS "registro";
CREATE TABLE registro (
usuario varchar(15) primary key,
password varchar(15),
idPersonaje int		
);
INSERT INTO "registro" VALUES('Nico','lala',1);
