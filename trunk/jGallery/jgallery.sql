# MySQL dump 8.16
#
# Host: localhost    Database: jgallery
#--------------------------------------------------------
# Server version	3.23.44-nt

#
# Table structure for table 'comments'
#

CREATE TABLE comments (
  id int(11) NOT NULL auto_increment,
  image_id int(11) default NULL,
  text text,
  PRIMARY KEY  (id)
) TYPE=MyISAM;

#
# Table structure for table 'folders'
#

CREATE TABLE folders (
  id int(11) NOT NULL auto_increment,
  folder varchar(255) default NULL,
  hits int(11) NOT NULL default '0',
  PRIMARY KEY  (id)
) TYPE=MyISAM;

#
# Table structure for table 'images'
#

CREATE TABLE images (
  id int(11) NOT NULL auto_increment,
  folder int(11) NOT NULL default '0',
  image varchar(50) default NULL,
  hits int(11) NOT NULL default '0',
  PRIMARY KEY  (id)
) TYPE=MyISAM;


