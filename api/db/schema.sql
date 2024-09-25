drop table if exists users;
create table users (
  id int not null auto_increment,
  username varchar(200) not null unique comment 'user name',
  email varchar(50) not null unique comment 'user email',
  password varchar(200) not null comment 'user password',
  age int not null default 18 comment 'age',
  gender int not null default 0 comment 'gender, 0 is femail, 1 is male',
  phone_number varchar(50) comment 'phone number',
  address varchar(500) comment 'address',
  avatar varchar(500) comment 'record the user avatar',
  created_at datetime not null default now() comment 'created date time',
  primary key (id)
) engine = InnoDB default charset = utf8 comment 'user table';

drop table if exists plant_categories;
create table plant_categories (
  id int not null auto_increment,
  name varchar(200) not null comment 'plant category name',
  description varchar(500) comment 'description',
  created_at datetime not null default now() comment 'created date time',
  primary key (id)
) engine = InnoDB default charset = utf8 comment 'plant category table';

drop table if exists plants;
create table plants (
  id int not null auto_increment,
  plant_category_id int not null comment 'plant category id',
  name varchar(200) not null comment 'plant name',
  light varchar(500) comment 'plant light condition',
  water varchar(500) comment 'plant water condition',
  soil varchar(500) comment 'plant soil condition',
  temperature varchar(500) comment 'plant temperature condition',
  humidity varchar(500) comment 'plant humidity condition',
  fertilization varchar(500) comment 'plant fertilization condition',
  harvesting varchar(500) comment 'plant harvesting condition',
  description varchar(500) comment 'description',
  plant_image varchar(500) comment 'plant image path',
  created_at datetime not null default now() comment 'created date time',
  primary key (id),
  foreign key (plant_category_id) references plant_categories (id)
) engine = InnoDB default charset = utf8 comment 'plant table';

drop table if exists plant_collections;
create table plant_collections (
  id int not null auto_increment,
  user_id int not null comment 'user id',
  plant_id int not null comment 'plant id',
  created_at datetime not null default now() comment 'created date time',
  primary key (id),
  foreign key (user_id) references users (id),
  foreign key (plant_id) references plants (id)
) engine = InnoDB default charset = utf8 comment 'plant collection table';

drop table if exists events;
create table events (
  id int not null auto_increment,
  user_id int not null comment 'event created user id',
  title varchar(300) not null comment 'event title',
  content varchar(500) not null comment 'event content',
  notified_datetime datetime not null comment 'event notified date time',
  created_at datetime not null default now() comment 'created date time',
  primary key (id),
  foreign key (user_id) references users (id)
) engine = InnoDB default charset = utf8 comment 'event table';

drop table if exists posts;
create table posts (
  id int not null auto_increment,
  creator_id int not null comment 'post created user id',
  title varchar(300) not null comment 'post title',
  content varchar(500) not null comment 'post content',
  created_at datetime not null default now() comment 'created date time',
  primary key (id),
  foreign key (creator_id) references users (id)
) engine = InnoDB default charset = utf8 comment 'posts table';

drop table if exists post_images;
create table post_images (
  id int not null auto_increment,
  post_id int not null comment 'post id',
  src varchar(300) not null comment 'post image path',
  created_at datetime not null default now() comment 'created date time',
  primary key (id),
  foreign key (post_id) references posts (id)
) engine = InnoDB default charset = utf8 comment 'posts image table';

drop table if exists post_comments;
create table post_comments (
  id int not null auto_increment,
  post_id int not null comment 'post id',
  comment_user_id int not null comment 'post comment user id',
  content varchar(500) comment 'post content',
  created_at datetime not null default now() comment 'created date time',
  primary key (id),
  foreign key (post_id) references posts (id),
  foreign key (comment_user_id) references users (id)
) engine = InnoDB default charset = utf8 comment 'post comment table';