# ------------------------------------------------------------------------
# I) Examples of joins.
# ------------------------------------------------------------------------

# ------------------------------------------------------------------------
# Note: Perl script that is used to generate a command line.
#
# use strict;
#
# my $SCRIPT = 'dbview.sh';
#
# if ($#ARGV == -1)
# {
#   print "Usage perl cli.pl table [table [table ...]]\n";
#   exit 1;
# }
#
# my $Template = <<EOS;
# #> ${SCRIPT} export -exporter dot-light -zoom {TABLES} -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > {NAME}_light.dot
# #> dot -Tgif -O{NAME}_light {NAME}_light.dot
# #> ${SCRIPT} export -exporter dot-medium -zoom {TABLES} -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > {NAME}_medium.dot
# #> dot -Tgif -O{NAME}_medium {NAME}_medium.dot
# #> ${SCRIPT} dbview.sh export -exporter dot-full -zoom {TABLES} -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > {NAME}_full.dot
# #> dot -Tgif -O{NAME}_full {NAME}_full.dot
# EOS
#
# my $TABLES = join(',', @ARGV);
# my $NAME   = join('_', @ARGV);
# my $T      = $Template;
#
# $T =~ s/\{TABLES\}/${TABLES}/mg;
# $T =~ s/\{NAME\}/${NAME}/mg;
# print "$T\n";
# ------------------------------------------------------------------------



# ---------------------------------
# 1. Soft foreign key
# ---------------------------------

#> dbview.sh export -exporter dot-light -zoom sfk_reference,sfk_dependent -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > sfk_light.dot
#> dot -Tgif -Osfk_light sfk_light.dot
#> dbview.sh export -exporter dot-medium -zoom sfk_reference,sfk_dependent -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > sfk_medium.dot
#> dot -Tgif -Osfk_medium sfk_medium.dot
#> dbview.sh export -exporter dot-full -zoom sfk_reference,sfk_dependent -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > sfk_full.dot
#> dot -Tgif -Osfk_full sfk_full.dot

DROP TABLE IF EXISTS sfk_reference;
DROP TABLE IF EXISTS sfk_dependent;

CREATE TABLE sfk_reference
(
    id INT NOT NULL,
    PRIMARY KEY(id)
)
ENGINE=MYISAM;

CREATE TABLE sfk_dependent
(
    some_field INT,
    fk_sfk_reference INT
)
ENGINE=MYISAM;

# ---------------------------------
# 2. Hard foreign key
# ---------------------------------

#> dbview.sh export -exporter dot-light -zoom hfk_reference,hfk_dependent -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > hfk_light.dot
#> dot -Tgif -Ohfk_light hfk_light.dot
#> dbview.sh export -exporter dot-medium -zoom hfk_reference,hfk_dependent -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > hfk_medium.dot
#> dot -Tgif -Ohfk_medium hfk_medium.dot
#> dbview.sh export -exporter dot-full -zoom hfk_reference,hfk_dependent -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > hfk_full.dot
#> dot -Tgif -Ohfk_full hfk_full.dot

SET foreign_key_checks = 0;
DROP TABLE IF EXISTS hfk_reference;
DROP TABLE IF EXISTS hfk_dependent;
SET foreign_key_checks = 1;

CREATE TABLE hfk_reference
(
    id INT NOT NULL,
    PRIMARY KEY(id)
)
ENGINE=INNODB;

CREATE TABLE hfk_dependent
(
    some_field INT,
    fk_hfk_reference_id INT,
    FOREIGN KEY (fk_hfk_reference_id) REFERENCES hfk_reference(id)
)
ENGINE=INNODB;

# ------------------------------------------------------------------------
# II) Cardinalities
# ------------------------------------------------------------------------

#       Foreign key -> Reference field
#
#  1.   M  (0..N)      M  (0..N)
#  2.   M  (0..N)      MN (1..N)
#  3.   M  (0..N)      U  (0..1)
#  4.   M  (0..N)      UN (1..1)
#  5.   MN (1..N)      M  (0..N)
#  6.   MN (1..N)      MN (1..N)
#  7.   MN (1..N)      U  (0..1)
#  8.   MN (1..N)      UN (1..1)
#  9.   U  (0..1)      M  (0..N)
#  10.  U  (0..1)      MN (1..N)
#  11.  U  (0..1)      U  (0..1)
#  12.  U  (0..1)      UN (1..1)
#  13.  UN (1..1)      M  (0..N)
#  14.  UN (1..1)      MN (1..N)
#  15.  UN (1..1)      U  (0..1)
#  16.  UN (1..1)      UN (1..1)
#
# With: M:  Multiple index, that may be NULL.
#       U:  Unique index, that may be NULL.
#       MN: Multiple index, that can not be NULL.
#       UN: Unique index, that can not be NULL.

# (minA..maxA) -> (minB..maxB)
#
# @A is associated with minA..maxB @B (minA = 0 ou 1, maxB = 1 ou N)
# => @A is associated with (0|1)..(1|N) @B
#
# @B is associated with 0..maxA @A (maxA = 1 ou N)
# => @B is associated with 0..(1|N) @A
#
# Cardinalities are:
#   ?: 0 or 1
#   +: 1 or more
#   *: 0 or more
#   1: 1 and only 1

# ------------------------------------------------------------------------
# Note: Perl script that is used to create the (16 folowwing) CLIs:
#
# use strict;
#
# my $SCRIPT = 'dbview.sh';
#
# my $Template = <<EOS;
# #> ${SCRIPT} export -exporter dot-light -zoom {TABLE1},{TABLE2} -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > {TABLE1}{TABLE2}_light.dot
# #> dot -Tgif -O{TABLE1}{TABLE2}_light {TABLE1}{TABLE2}_light.dot
# #> ${SCRIPT} export -exporter dot-medium -zoom {TABLE1},{TABLE2} -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > {TABLE1}{TABLE2}_medium.dot
# #> dot -Tgif -O{TABLE1}{TABLE2}_medium {TABLE1}{TABLE2}_medium.dot
# #> ${SCRIPT} dbview.sh export -exporter dot-full -zoom {TABLE1},{TABLE2} -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > {TABLE1}{TABLE2}_full.dot
# #> dot -Tgif -O{TABLE1}{TABLE2}_full {TABLE1}{TABLE2}_full.dot
# EOS
#
# for (my $i=1; $i<=16; $i++)
# {
#   my $table1 = "table${i}a";
#   my $table2 = "table${i}b";
#   my $T      = $Template;
#
#   $T =~ s/\{TABLE1\}/${table1}/mg;
#   $T =~ s/\{TABLE2\}/${table2}/mg;
#
#   print "$T\n";
# }
# ------------------------------------------------------------------------


# ------------------------------------------------------------------------
# 1) M (0..N) -> M (0..N)
#    @A is associated with 0..N @B.
#    @B is associated with 0..N @A
# => * @A @B *
#
#> dbview.sh export -exporter dot-light -zoom table1a,table1b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table1atable1b_light.dot
#> dot -Tgif -Otable1atable1b_light table1atable1b_light.dot
#> dbview.sh export -exporter dot-medium -zoom table1a,table1b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table1atable1b_medium.dot
#> dot -Tgif -Otable1atable1b_medium table1atable1b_medium.dot
#> dbview.sh dbview.sh export -exporter dot-full -zoom table1a,table1b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table1atable1b_full.dot
#> dot -Tgif -Otable1atable1b_full table1atable1b_full.dot
# ------------------------------------------------------------------------

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS table1A;
  DROP TABLE IF EXISTS table1B;

SET foreign_key_checks = 1;

CREATE TABLE table1B
(
   target INT,
   INDEX mindex (target)
)
ENGINE=INNODB;

INSERT INTO table1B SET target=10;
INSERT INTO table1B SET target=10;
INSERT INTO table1B SET target=10;

CREATE TABLE table1A
(
   primary_key INT NOT NULL AUTO_INCREMENT,
   foreign_key INT,
   INDEX mindex (foreign_key),
   PRIMARY KEY (primary_key),
   FOREIGN KEY (foreign_key) REFERENCES table1B(target)
)
ENGINE=INNODB;

INSERT INTO table1A SET foreign_key=10;
INSERT INTO table1A SET foreign_key=10;

# ------------------------------------------------------------------------
# 2) M (0..N) -> MN (1..N)
#    @A is associated with 0..N @B
#    @B is associated with 0..N @A
# => * @A @B *
#
#> dbview.sh export -exporter dot-light -zoom table2a,table2b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table2atable2b_light.dot
#> dot -Tgif -Otable2atable2b_light table2atable2b_light.dot
#> dbview.sh export -exporter dot-medium -zoom table2a,table2b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table2atable2b_medium.dot
#> dot -Tgif -Otable2atable2b_medium table2atable2b_medium.dot
#> dbview.sh dbview.sh export -exporter dot-full -zoom table2a,table2b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table2atable2b_full.dot
#> dot -Tgif -Otable2atable2b_full table2atable2b_full.dot
# ------------------------------------------------------------------------

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS table2A;
  DROP TABLE IF EXISTS table2B;

SET foreign_key_checks = 1;

CREATE TABLE table2B	(
              target INT NOT NULL,
              INDEX mindex (target)
                      )
                      ENGINE=INNODB;

CREATE TABLE table2A	(
              primary_key INT NOT NULL,
              foreign_key INT,
              INDEX mindex (foreign_key),
              PRIMARY KEY(primary_key),
              FOREIGN KEY (foreign_key) REFERENCES table2B(target)
            )
            ENGINE=INNODB;

# ------------------------------------------------------------------------
# 3) M (0..N) => U (0..1)
#    @A is associated with 0..1 @B
#    @B is associated with 0..N @A
# => * @A @B ?
#
#> dbview.sh export -exporter dot-light -zoom table3a,table3b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table3atable3b_light.dot
#> dot -Tgif -Otable3atable3b_light table3atable3b_light.dot
#> dbview.sh export -exporter dot-medium -zoom table3a,table3b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table3atable3b_medium.dot
#> dot -Tgif -Otable3atable3b_medium table3atable3b_medium.dot
#> dbview.sh dbview.sh export -exporter dot-full -zoom table3a,table3b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table3atable3b_full.dot
#> dot -Tgif -Otable3atable3b_full table3atable3b_full.dot
# ------------------------------------------------------------------------

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS table3A;
  DROP TABLE IF EXISTS table3B;

SET foreign_key_checks = 1;

CREATE TABLE table3B	(
              target INT,
              UNIQUE INDEX mindex (target)
                      )
                      ENGINE=INNODB;

CREATE TABLE table3A	(
              primary_key INT NOT NULL,
              foreign_key INT,
              INDEX mindex (foreign_key),
              PRIMARY KEY(primary_key),
              FOREIGN KEY (foreign_key) REFERENCES table3B(target)
            )
            ENGINE=INNODB;

# ------------------------------------------------------------------------
# 4) M (0..N) -> UN (1..1)
#    @A is associated with 0..1 @B
#    @B is associated with 0..N @A
# => * @A @B ?
#
#> dbview.sh export -exporter dot-light -zoom table4a,table4b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table4atable4b_light.dot
#> dot -Tgif -Otable4atable4b_light table4atable4b_light.dot
#> dbview.sh export -exporter dot-medium -zoom table4a,table4b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table4atable4b_medium.dot
#> dot -Tgif -Otable4atable4b_medium table4atable4b_medium.dot
#> dbview.sh dbview.sh export -exporter dot-full -zoom table4a,table4b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table4atable4b_full.dot
#> dot -Tgif -Otable4atable4b_full table4atable4b_full.dot
# ------------------------------------------------------------------------

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS table4A;
  DROP TABLE IF EXISTS table4B;

SET foreign_key_checks = 1;

CREATE TABLE table4B	(
              target INT NOT NULL,
              UNIQUE INDEX mindex (target)
                      )
                      ENGINE=INNODB;

CREATE TABLE table4A	(
              primary_key INT NOT NULL,
              foreign_key INT,
              INDEX mindex (foreign_key),
              PRIMARY KEY(primary_key),
              FOREIGN KEY (foreign_key) REFERENCES table4B(target)
            )
            ENGINE=INNODB;

# ------------------------------------------------------------------------
# 5) MN (1..N) -> M (0..N)
#    @A is associated with 1..N @B
#    @B is associated with 0..N @A
# => * @A @B +
#
#> dbview.sh export -exporter dot-light -zoom table5a,table5b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table5atable5b_light.dot
#> dot -Tgif -Otable5atable5b_light table5atable5b_light.dot
#> dbview.sh export -exporter dot-medium -zoom table5a,table5b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table5atable5b_medium.dot
#> dot -Tgif -Otable5atable5b_medium table5atable5b_medium.dot
#> dbview.sh dbview.sh export -exporter dot-full -zoom table5a,table5b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table5atable5b_full.dot
#> dot -Tgif -Otable5atable5b_full table5atable5b_full.dot
# ------------------------------------------------------------------------

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS table5A;
  DROP TABLE IF EXISTS table5B;

SET foreign_key_checks = 1;

CREATE TABLE table5B	(
              target INT,
              INDEX mindex (target)
                      )
                      ENGINE=INNODB;

CREATE TABLE table5A	(
              primary_key INT NOT NULL,
              foreign_key INT NOT NULL,
              INDEX mindex (foreign_key),
              PRIMARY KEY(primary_key),
              FOREIGN KEY (foreign_key) REFERENCES table5B(target)
            )
            ENGINE=INNODB;

# ------------------------------------------------------------------------
# 6) MN (1..N) -> MN (1..N)
#    @A is associated with 1..N @B
#    @B is associated with 0..N @A
# => * @A @B +
#
#> dbview.sh export -exporter dot-light -zoom table6a,table6b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table6atable6b_light.dot
#> dot -Tgif -Otable6atable6b_light table6atable6b_light.dot
#> dbview.sh export -exporter dot-medium -zoom table6a,table6b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table6atable6b_medium.dot
#> dot -Tgif -Otable6atable6b_medium table6atable6b_medium.dot
#> dbview.sh dbview.sh export -exporter dot-full -zoom table6a,table6b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table6atable6b_full.dot
#> dot -Tgif -Otable6atable6b_full table6atable6b_full.dot
# ------------------------------------------------------------------------

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS table6A;
  DROP TABLE IF EXISTS table6B;

SET foreign_key_checks = 1;

CREATE TABLE table6B	(
              target INT NOT NULL,
              INDEX mindex (target)
                      )
                      ENGINE=INNODB;

CREATE TABLE table6A	(
              primary_key INT NOT NULL,
              foreign_key INT NOT NULL,
              INDEX mindex (foreign_key),
              PRIMARY KEY(primary_key),
              FOREIGN KEY (foreign_key) REFERENCES table6B(target)
            )
            ENGINE=INNODB;

# ------------------------------------------------------------------------
# 7) MN (1..N) => U (0..1)
#    @A is associated with 1..1 @B
#    @B is associated with 0..N @A
# => * @A @B 1
#
#> dbview.sh export -exporter dot-light -zoom table7a,table7b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table7atable7b_light.dot
#> dot -Tgif -Otable7atable7b_light table7atable7b_light.dot
#> dbview.sh export -exporter dot-medium -zoom table7a,table7b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table7atable7b_medium.dot
#> dot -Tgif -Otable7atable7b_medium table7atable7b_medium.dot
#> dbview.sh dbview.sh export -exporter dot-full -zoom table7a,table7b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table7atable7b_full.dot
#> dot -Tgif -Otable7atable7b_full table7atable7b_full.dot
# ------------------------------------------------------------------------

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS table7A;
  DROP TABLE IF EXISTS table7B;

SET foreign_key_checks = 1;

CREATE TABLE table7B	(
              target INT,
              UNIQUE INDEX mindex (target)
                      )
                      ENGINE=INNODB;

CREATE TABLE table7A	(
              primary_key INT NOT NULL,
              foreign_key INT NOT NULL,
              INDEX mindex (foreign_key),
              PRIMARY KEY(primary_key),
              FOREIGN KEY (foreign_key) REFERENCES table7B(target)
            )
            ENGINE=INNODB;

# ------------------------------------------------------------------------
# 8) MN (1..N) => UN (1..1)
#    @A is associated with 1..1 @B
#    @B is associated with 0..N @A
# => * @A @B 1
#
#> dbview.sh export -exporter dot-light -zoom table8a,table8b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table8atable8b_light.dot
#> dot -Tgif -Otable8atable8b_light table8atable8b_light.dot
#> dbview.sh export -exporter dot-medium -zoom table8a,table8b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table8atable8b_medium.dot
#> dot -Tgif -Otable8atable8b_medium table8atable8b_medium.dot
#> dbview.sh dbview.sh export -exporter dot-full -zoom table8a,table8b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table8atable8b_full.dot
#> dot -Tgif -Otable8atable8b_full table8atable8b_full.dot
# ------------------------------------------------------------------------

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS table8A;
  DROP TABLE IF EXISTS table8B;

SET foreign_key_checks = 1;

CREATE TABLE table8B (
                       target INT NOT NULL,
                       UNIQUE INDEX mindex (target)
                     )
                     ENGINE=INNODB;

CREATE TABLE table8A	(
              primary_key INT NOT NULL,
              foreign_key INT NOT NULL,
              INDEX mindex (foreign_key),
              PRIMARY KEY(primary_key),
              FOREIGN KEY (foreign_key) REFERENCES table8B(target)
            )
            ENGINE=INNODB;

# ------------------------------------------------------------------------
# 9) U (0..1) => M (0..N)
#    @A is associated with 0..N @B
#    @B is associated with 0..1 @A
# => ? @A @B *
#
#> dbview.sh export -exporter dot-light -zoom table9a,table9b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table9atable9b_light.dot
#> dot -Tgif -Otable9atable9b_light table9atable9b_light.dot
#> dbview.sh export -exporter dot-medium -zoom table9a,table9b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table9atable9b_medium.dot
#> dot -Tgif -Otable9atable9b_medium table9atable9b_medium.dot
#> dbview.sh dbview.sh export -exporter dot-full -zoom table9a,table9b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table9atable9b_full.dot
#> dot -Tgif -Otable9atable9b_full table9atable9b_full.dot
# ------------------------------------------------------------------------

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS table9A;
  DROP TABLE IF EXISTS table9B;

SET foreign_key_checks = 1;

CREATE TABLE table9B (
                       target INT,
                       INDEX mindex (target)
                     )
                     ENGINE=INNODB;

CREATE TABLE table9A	(
              primary_key INT NOT NULL,
              foreign_key INT,
              UNIQUE INDEX uindex (foreign_key),
              PRIMARY KEY(primary_key),
              FOREIGN KEY (foreign_key) REFERENCES table9B(target)
            )
            ENGINE=INNODB;

# ------------------------------------------------------------------------
# 10) U (0..1) => MN (1..N)
#     @A is associated with 0..N @B
#     @B is associated with 0..1 @A
# =>  ? @A @B *
#
#> dbview.sh export -exporter dot-light -zoom table10a,table10b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table10atable10b_light.dot
#> dot -Tgif -Otable10atable10b_light table10atable10b_light.dot
#> dbview.sh export -exporter dot-medium -zoom table10a,table10b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table10atable10b_medium.dot
#> dot -Tgif -Otable10atable10b_medium table10atable10b_medium.dot
#> dbview.sh dbview.sh export -exporter dot-full -zoom table10a,table10b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table10atable10b_full.dot
#> dot -Tgif -Otable10atable10b_full table10atable10b_full.dot
# ------------------------------------------------------------------------

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS table10A;
  DROP TABLE IF EXISTS table10B;

SET foreign_key_checks = 1;

CREATE TABLE table10B (
                       target INT NOT NULL,
                       INDEX mindex (target)
                     )
                     ENGINE=INNODB;

CREATE TABLE table10A	(
              primary_key INT NOT NULL,
              foreign_key INT,
              UNIQUE INDEX uindex (foreign_key),
              PRIMARY KEY(primary_key),
              FOREIGN KEY (foreign_key) REFERENCES table10B(target)
            )
            ENGINE=INNODB;

# ------------------------------------------------------------------------
# 11) U (0..1) => MN (1..N)
#     @A is associated with 0..N @B
#     @B is associated with 0..1 @A
# =>  ? @A @B *
#
#> dbview.sh export -exporter dot-light -zoom table11a,table11b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table11atable11b_light.dot
#> dot -Tgif -Otable11atable11b_light table11atable11b_light.dot
#> dbview.sh export -exporter dot-medium -zoom table11a,table11b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table11atable11b_medium.dot
#> dot -Tgif -Otable11atable11b_medium table11atable11b_medium.dot
#> dbview.sh dbview.sh export -exporter dot-full -zoom table11a,table11b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table11atable11b_full.dot
#> dot -Tgif -Otable11atable11b_full table11atable11b_full.dot
# ------------------------------------------------------------------------

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS table11A;
  DROP TABLE IF EXISTS table11B;

SET foreign_key_checks = 1;

CREATE TABLE table11B (
                       target INT NOT NULL,
                       INDEX mindex (target)
                     )
                     ENGINE=INNODB;

CREATE TABLE table11A	(
              primary_key INT NOT NULL,
              foreign_key INT,
              UNIQUE INDEX uindex (foreign_key),
              PRIMARY KEY(primary_key),
              FOREIGN KEY (foreign_key) REFERENCES table11B(target)
            )
            ENGINE=INNODB;

# ------------------------------------------------------------------------
# 12) U (0..1) => UN (1..1)
#     @A is associated with 0..1 @B
#     @B is associated with 0..1 @A
# =>  ? @A @B ?
#
#> dbview.sh export -exporter dot-light -zoom table12a,table12b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table12atable12b_light.dot
#> dot -Tgif -Otable12atable12b_light table12atable12b_light.dot
#> dbview.sh export -exporter dot-medium -zoom table12a,table12b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table12atable12b_medium.dot
#> dot -Tgif -Otable12atable12b_medium table12atable12b_medium.dot
#> dbview.sh dbview.sh export -exporter dot-full -zoom table12a,table12b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table12atable12b_full.dot
#> dot -Tgif -Otable12atable12b_full table12atable12b_full.dot
# ------------------------------------------------------------------------

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS table12A;
  DROP TABLE IF EXISTS table12B;

SET foreign_key_checks = 1;

CREATE TABLE table12B (
                       target INT NOT NULL,
                       UNIQUE INDEX uindex (target)
                     )
                     ENGINE=INNODB;

CREATE TABLE table12A	(
              primary_key INT NOT NULL,
              foreign_key INT,
              UNIQUE INDEX uindex (foreign_key),
              PRIMARY KEY(primary_key),
              FOREIGN KEY (foreign_key) REFERENCES table12B(target)
            )
            ENGINE=INNODB;

# ------------------------------------------------------------------------
# 13) UN (1..1) => M (0..N)
#     @A is associated with 1..N @B
#     @B is associated with 0..1 @A
# =>  ? @A @B +
#
#> dbview.sh export -exporter dot-light -zoom table13a,table13b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table13atable13b_light.dot
#> dot -Tgif -Otable13atable13b_light table13atable13b_light.dot
#> dbview.sh export -exporter dot-medium -zoom table13a,table13b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table13atable13b_medium.dot
#> dot -Tgif -Otable13atable13b_medium table13atable13b_medium.dot
#> dbview.sh dbview.sh export -exporter dot-full -zoom table13a,table13b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table13atable13b_full.dot
#> dot -Tgif -Otable13atable13b_full table13atable13b_full.dot
# ------------------------------------------------------------------------

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS table13A;
  DROP TABLE IF EXISTS table13B;

SET foreign_key_checks = 1;

CREATE TABLE table13B (
                       target INT,
                       INDEX mindex (target)
                     )
                     ENGINE=INNODB;

CREATE TABLE table13A	(
              primary_key INT NOT NULL,
              foreign_key INT NOT NULL,
              UNIQUE INDEX uindex (foreign_key),
              PRIMARY KEY(primary_key),
              FOREIGN KEY (foreign_key) REFERENCES table13B(target)
            )
            ENGINE=INNODB;

# ------------------------------------------------------------------------
# 14) UN (1..1) => MN (1..N)
#     @A is associated with 1..N @B
#     @B is associated with 0..1 @A
# =>  ? @A @B +
#
#> dbview.sh export -exporter dot-light -zoom table14a,table14b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table14atable14b_light.dot
#> dot -Tgif -Otable14atable14b_light table14atable14b_light.dot
#> dbview.sh export -exporter dot-medium -zoom table14a,table14b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table14atable14b_medium.dot
#> dot -Tgif -Otable14atable14b_medium table14atable14b_medium.dot
#> dbview.sh dbview.sh export -exporter dot-full -zoom table14a,table14b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table14atable14b_full.dot
#> dot -Tgif -Otable14atable14b_full table14atable14b_full.dot
# ------------------------------------------------------------------------

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS table14A;
  DROP TABLE IF EXISTS table14B;

SET foreign_key_checks = 1;

CREATE TABLE table14B (
                       target INT NOT NULL,
                       INDEX mindex (target)
                     )
                     ENGINE=INNODB;

CREATE TABLE table14A	(
              primary_key INT NOT NULL,
              foreign_key INT NOT NULL,
              UNIQUE INDEX uindex (foreign_key),
              PRIMARY KEY(primary_key),
              FOREIGN KEY (foreign_key) REFERENCES table14B(target)
            )
            ENGINE=INNODB;

# ------------------------------------------------------------------------
# 15) UN (1..1) => U (0..1)
#     @A is associated with 1..1 @B
#     @B is associated with 0..1 @A
# =>  ? @A @B 1
#
#> dbview.sh export -exporter dot-light -zoom table15a,table15b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table15atable15b_light.dot
#> dot -Tgif -Otable15atable15b_light table15atable15b_light.dot
#> dbview.sh export -exporter dot-medium -zoom table15a,table15b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table15atable15b_medium.dot
#> dot -Tgif -Otable15atable15b_medium table15atable15b_medium.dot
#> dbview.sh dbview.sh export -exporter dot-full -zoom table15a,table15b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table15atable15b_full.dot
#> dot -Tgif -Otable15atable15b_full table15atable15b_full.dot
# ------------------------------------------------------------------------

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS table15A;
  DROP TABLE IF EXISTS table15B;

SET foreign_key_checks = 1;

CREATE TABLE table15B (
                       target INT,
                       UNIQUE INDEX uindex (target)
                     )
                     ENGINE=INNODB;

CREATE TABLE table15A	(
              primary_key INT NOT NULL,
              foreign_key INT NOT NULL,
              UNIQUE INDEX uindex (foreign_key),
              PRIMARY KEY(primary_key),
              FOREIGN KEY (foreign_key) REFERENCES table15B(target)
            )
            ENGINE=INNODB;

# ------------------------------------------------------------------------
# 16) UN (1..1) => UN (1..1)
#     @A is associated with 1..1 @B
#     @B is associated with 0..1 @A
# =>  ? @A @B 1
#
#> dbview.sh export -exporter dot-light -zoom table16a,table16b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table16atable16b_light.dot
#> dot -Tgif -Otable16atable16b_light table16atable16b_light.dot
#> dbview.sh export -exporter dot-medium -zoom table16a,table16b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table16atable16b_medium.dot
#> dot -Tgif -Otable16atable16b_medium table16atable16b_medium.dot
#> dbview.sh dbview.sh export -exporter dot-full -zoom table16a,table16b -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > table16atable16b_full.dot
#> dot -Tgif -Otable16atable16b_full table16atable16b_full.dot
# ------------------------------------------------------------------------

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS table16A;
  DROP TABLE IF EXISTS table16B;

SET foreign_key_checks = 1;

CREATE TABLE table16B (
                       target INT NOT NULL,
                       UNIQUE INDEX uindex (target)
                     )
                     ENGINE=INNODB;

CREATE TABLE table16A	(
              primary_key INT NOT NULL,
              foreign_key INT NOT NULL,
              UNIQUE INDEX uindex (foreign_key),
              PRIMARY KEY(primary_key),
              FOREIGN KEY (foreign_key) REFERENCES table16B(target)
            )
            ENGINE=INNODB;


# ------------------------------------------------------------------------
# III) Loop
# ------------------------------------------------------------------------

#> dbview.sh export -exporter dot-light -zoom loopa,loopb -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > loop_light.dot
#> dot -Tgif -Oloop_light loop_light.dot
#> dbview.sh export -exporter dot-medium -zoom loopa,loopb -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > loop_medium.dot
#> dot -Tgif -Oloop_medium loop_medium.dot
#> dbview.sh export -exporter dot-full -zoom loopa,loopb -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > loop_full.dot
#> dot -Tgif -Oloop_full loop_full.dot

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS loopA;
  DROP TABLE IF EXISTS loopB;


  CREATE TABLE loopB (
                           primary_key INT NOT NULL,
                           foreign_key INT NOT NULL,
                           PRIMARY KEY(primary_key),
                           UNIQUE INDEX uindex (foreign_key),
                           FOREIGN KEY (foreign_key) REFERENCES loopA(primary_key)
                      )
                      ENGINE=INNODB;

  CREATE TABLE loopA	(
                           primary_key INT NOT NULL,
                           foreign_key INT NOT NULL,
                           UNIQUE INDEX uindex (foreign_key),
                           PRIMARY KEY(primary_key),
                           FOREIGN KEY (foreign_key) REFERENCES loopB(primary_key)
                        )
                        ENGINE=INNODB;


SET foreign_key_checks = 1;

# ------------------------------------------------------------------------
# IV) Self loop
# ------------------------------------------------------------------------

#> dbview.sh export -exporter dot-light -zoom selfloop -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > selfloop_light.dot
#> dot -Tgif -Oselfloop_light selfloop_light.dot
#> dbview.sh export -exporter dot-medium -zoom selfloop -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > selfloop_medium.dot
#> dot -Tgif -Oselfloop_medium selfloop_medium.dot
#> dbview.sh export -exporter dot-full -zoom selfloop -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > selfloop_full.dot
#> dot -Tgif -Oselfloop_full selfloop_full.dot

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS selfloop;

  CREATE TABLE selfloop ( id INT NOT NULL,
                          fkd INT,
                          FOREIGN KEY (fkd) REFERENCES selfloop(id),
                          PRIMARY KEY(id))
                          ENGINE=INNODB;

SET foreign_key_checks = 1;

#> dbview.sh export -exporter dot-light -zoom selfdoubleloop -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > selfdoubleloop_light.dot
#> dot -Tgif -Oselfdoubleloop_light selfdoubleloop_light.dot
#> dbview.sh export -exporter dot-medium -zoom selfdoubleloop -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > selfdoubleloop_medium.dot
#> dot -Tgif -Oselfdoubleloop_medium selfdoubleloop_medium.dot
#> dbview.sh export -exporter dot-full -zoom selfdoubleloop -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > selfdoubleloop_full.dot
#> dot -Tgif -Oselfdoubleloop_full selfdoubleloop_full.dot

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS selfdoubleloop;

  CREATE TABLE selfdoubleloop ( id INT NOT NULL,
                          		fkd1 INT,
                          		fkd2 INT,
                          		FOREIGN KEY (fkd1) REFERENCES selfdoubleloop(id),
                          		FOREIGN KEY (fkd2) REFERENCES selfdoubleloop(id),
                          		PRIMARY KEY(id))
                          		ENGINE=INNODB;

SET foreign_key_checks = 1;



# ------------------------------------------------------------------------
# V) Multiple pathes
# ------------------------------------------------------------------------

#> dbview.sh export -exporter dot-light -zoom beginning,l1a,l1b,l1c,l2a,l2b,l2c,l3b,l3c,arrival -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > pathes_light.dot
#> dot -Tgif -Opathes_light pathes_light.dot
#> dbview.sh export -exporter dot-medium -zoom beginning,l1a,l1b,l1c,l2a,l2b,l2c,l3b,l3c,arrival -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > pathes_medium.dot
#> dot -Tgif -Opathes_medium pathes_medium.dot
#> dbview.sh export -exporter dot-full -zoom beginning,l1a,l1b,l1c,l2a,l2b,l2c,l3b,l3c,arrival -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > pathes_full.dot
#> dot -Tgif -Opathes_full pathes_full.dot

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS Beginning;
  DROP TABLE IF EXISTS L1A;
  DROP TABLE IF EXISTS L1B;
  DROP TABLE IF EXISTS L1C;
  DROP TABLE IF EXISTS L2A;
  DROP TABLE IF EXISTS L2B;
  DROP TABLE IF EXISTS L2C;
  DROP TABLE IF EXISTS L3B;
  DROP TABLE IF EXISTS L3C;
  DROP TABLE IF EXISTS Arrival;

SET foreign_key_checks = 1;

  ## Bottom level

  CREATE TABLE Arrival (
                           id INT NOT NULL AUTO_INCREMENT,
                           PRIMARY KEY(id)
                       )
                       ENGINE=INNODB;

  ## Level 3

  CREATE TABLE L3B (
                       id INT NOT NULL AUTO_INCREMENT,
                       fkd INT,
                       PRIMARY KEY(id),
                       FOREIGN KEY (fkd) REFERENCES Arrival(id)
                   )
                   ENGINE=INNODB;

  CREATE TABLE L3C (
                       id INT NOT NULL AUTO_INCREMENT,
                       fkd INT,
                       PRIMARY KEY(id),
                       FOREIGN KEY (fkd) REFERENCES Arrival(id)
                   )
                   ENGINE=INNODB;

  ## Level 2

  CREATE TABLE L2A (
                       id INT NOT NULL AUTO_INCREMENT,
                       fkd INT,
                       PRIMARY KEY(id),
                       FOREIGN KEY (fkd) REFERENCES Arrival(id)
                   )
                   ENGINE=INNODB;

  CREATE TABLE L2B (
                       id INT NOT NULL AUTO_INCREMENT,
                       fkd INT,
                       PRIMARY KEY(id),
                       FOREIGN KEY (fkd) REFERENCES L3B(id)
                   )
                   ENGINE=INNODB;

  CREATE TABLE L2C (
                       id INT NOT NULL AUTO_INCREMENT,
                       fkd INT,
                       PRIMARY KEY(id),
                       FOREIGN KEY (fkd) REFERENCES L3C(id)
                   )
                   ENGINE=INNODB;

  ## Level 1

  CREATE TABLE L1A (
                       id INT NOT NULL AUTO_INCREMENT,
                       fkd INT,
                       PRIMARY KEY(id),
                       FOREIGN KEY (fkd) REFERENCES L2A(id)
                   )
                   ENGINE=INNODB;

  CREATE TABLE L1B (
                       id INT NOT NULL AUTO_INCREMENT,
                       fkd INT,
                       PRIMARY KEY(id),
                       FOREIGN KEY (fkd) REFERENCES L2B(id)
                   )
                   ENGINE=INNODB;

  CREATE TABLE L1C (
                       id INT NOT NULL AUTO_INCREMENT,
                       fkd3 INT,
                       fkd2 INT,
                       PRIMARY KEY(id),
                       FOREIGN KEY (fkd2) REFERENCES L2C(id),
                       FOREIGN KEY (fkd3) REFERENCES L3C(id)
                   )
                   ENGINE=INNODB;

  ## Top level

  CREATE TABLE Beginning (
                             id INT NOT NULL AUTO_INCREMENT,
                             fkdA INT,
                             fkdB INT,
                             fkdC INT,
                             FOREIGN KEY (fkdA) REFERENCES L1A(id),
                             FOREIGN KEY (fkdB) REFERENCES L1B(id),
                             FOREIGN KEY (fkdC) REFERENCES L1C(id),
                             PRIMARY KEY(id)
                         )
                         ENGINE=INNODB;





# ------------------------------------------------------------------------
# VI) Complexe
# ------------------------------------------------------------------------

#> dbview.sh export -exporter dot-light -zoom t1,t2,t3,t4,t5,t6,t7,t8 -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > pathes_light.dot
#> dot -Tgif -Opathes_light pathes_light.dot
#> dbview.sh export -exporter dot-medium -zoom t1,t2,t3,t4,t5,t6,t7,t8 -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > pathes_medium.dot
#> dot -Tgif -Opathes_medium pathes_medium.dot
#> dbview.sh export -exporter dot-full -zoom t1,t2,t3,t4,t5,t6,t7,t8 -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > pathes_full.dot
#> dot -Tgif -Opathes_full pathes_full.dot

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS t1;
  DROP TABLE IF EXISTS t2;
  DROP TABLE IF EXISTS t3;
  DROP TABLE IF EXISTS t4;
  DROP TABLE IF EXISTS t5;
  DROP TABLE IF EXISTS t6;
  DROP TABLE IF EXISTS t7;
  DROP TABLE IF EXISTS t8;

SET foreign_key_checks = 1;

  CREATE TABLE t8 (
                     id INT NOT NULL AUTO_INCREMENT,
                     PRIMARY KEY(id)
                  )
                  ENGINE=INNODB;

  CREATE TABLE t7 (
                     id INT NOT NULL AUTO_INCREMENT,
                     fk INT NOT NULL,
                     FOREIGN KEY (fk) REFERENCES t8(id),
                     PRIMARY KEY(id)
                  )
                  ENGINE=INNODB;

  CREATE TABLE t6 (
                     id INT NOT NULL AUTO_INCREMENT,
                     fk INT NOT NULL,
                     FOREIGN KEY (fk) REFERENCES t7(id),
                     PRIMARY KEY(id)
                  )
                  ENGINE=INNODB;

  CREATE TABLE t5 (
                     id INT NOT NULL AUTO_INCREMENT,
                     fk INT NOT NULL,
                     FOREIGN KEY (fk) REFERENCES t6(id),
                     PRIMARY KEY(id)
                  )
                  ENGINE=INNODB;

  CREATE TABLE t4 (
                     id INT NOT NULL AUTO_INCREMENT,
                     PRIMARY KEY(id)
                  )
                  ENGINE=INNODB;

  CREATE TABLE t3 (
                     id INT NOT NULL AUTO_INCREMENT,
                     fk1 INT NOT NULL,
                     fk2 INT NOT NULL,
                     FOREIGN KEY (fk1) REFERENCES t4(id),
                     FOREIGN KEY (fk2) REFERENCES t6(id),
                     PRIMARY KEY(id)
                  )
                  ENGINE=INNODB;

  CREATE TABLE t2 (
                     id INT NOT NULL AUTO_INCREMENT,
                     fk INT NOT NULL,
                     FOREIGN KEY (fk) REFERENCES t3(id),
                     PRIMARY KEY(id)
                  )
                  ENGINE=INNODB;

  CREATE TABLE t1 (
                     id INT NOT NULL AUTO_INCREMENT,
                     fk INT NOT NULL,
                     FOREIGN KEY (fk) REFERENCES t2(id),
                     PRIMARY KEY(id)
                  )
                  ENGINE=INNODB;



# ------------------------------------------------------------------------
# VII) Indexes
# ------------------------------------------------------------------------

#> dbview.sh export -exporter dot-light -zoom indexTable -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > pathes_light.dot
#> dot -Tgif -Opathes_light pathes_light.dot
#> dbview.sh export -exporter dot-medium -zoom indexTable -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > pathes_medium.dot
#> dot -Tgif -Opathes_medium pathes_medium.dot
#> dbview.sh export -exporter dot-full -zoom indexTable -db-adaptor mysql -host localhost -user root -password root -dbname mydb -port 8889 > pathes_full.dot
#> dot -Tgif -Opathes_full pathes_full.dot

SET foreign_key_checks = 0;

  DROP TABLE IF EXISTS indexTable;

SET foreign_key_checks = 1;

  CREATE TABLE indexTable (
    	                     id INT NOT NULL AUTO_INCREMENT,
                             id1 INT NOT NULL,
                             id2 INT NOT NULL,
                             id3 INT NOT NULL,
                             id4 INT NOT NULL,
                             id5 INT NOT NULL,
                             PRIMARY KEY(id),
                             UNIQUE INDEX idx1 (id1),
                             UNIQUE INDEX idx3 (id2, id3),
                             INDEX idx4 (id4, id5)
                           )
                           ENGINE=INNODB;












