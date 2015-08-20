create database personal_finance;
use personal_finance;

create table trade_record (
	id int primary key auto_increment,
	reason varchar(128),
	catagory smallint,
	amt_flag smallint,
	amt double,
	update_time timestamp
)

create table pf_enum (
	id int primary key auto_increment,
	enum_key smallint,
	enum_value varchar(128),
	enum_catagory varchar(30)
)
INSERT INTO `personal_finance`.`pf_enum` (`id`, `enum_key`, `enum_value`, `enum_catagory`) VALUES ('1', '1', '收', 'amtFlag');
INSERT INTO `personal_finance`.`pf_enum` (`id`, `enum_key`, `enum_value`, `enum_catagory`) VALUES ('2', '2', '支', 'amtFlag');
INSERT INTO `personal_finance`.`pf_enum` (`id`, `enum_key`, `enum_value`, `enum_catagory`) VALUES ('3', '1', '食品', 'catagory');
INSERT INTO `personal_finance`.`pf_enum` (`id`, `enum_key`, `enum_value`, `enum_catagory`) VALUES ('4', '2', '衣服', 'catagory');
INSERT INTO `personal_finance`.`pf_enum` (`id`, `enum_key`, `enum_value`, `enum_catagory`) VALUES ('5', '3', '人情', 'catagory');
INSERT INTO `personal_finance`.`pf_enum` (`id`, `enum_key`, `enum_value`, `enum_catagory`) VALUES ('6', '4', '玩乐', 'catagory');
INSERT INTO `personal_finance`.`pf_enum` (`id`, `enum_key`, `enum_value`, `enum_catagory`) VALUES ('7', '5', '交通费', 'catagory');
