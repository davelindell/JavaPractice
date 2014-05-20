create table user
(
	user_id integer not null primary key autoincrement,
	user_first_name varchar(255) not null,
	user_last_name varchar(255) not null,
	username varchar(255) not null,
        password varchar(255) not null,
        num_records integer not null
);

create table projects
(
	project_id integer not null primary key autoincrement,
        project_title varchar(255) not null
);

create table batches 
(
	batch_id integer not null primary key autoincrement,
	project_id integer not null,        
	image_url varchar(255) not null,
	first_y_coord integer not null,
	record_height integer not null,
	num_records integer not null,
	num_fields integer not null
);

create table fields 
(
	project_id integer not null,
	field_id integer not null primary key autoincrement,
	field_title varchar(255) not null,
	help_url varchar(255) not null,
	x_coord integer not null,
	pixel_width integer not null,
	known_values_url varchar(255)	
);

create table indexed_data (
	data_id integer not null primary key autoincrement,	
	batch_id integer not null,	
	record_number integer not null,	
	field_id integer not null,
	record_value varchar(255) not null	
);


