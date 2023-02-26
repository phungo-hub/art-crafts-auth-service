 create table userRole.hibernate_sequence
 (
     next_val bigint null
 );

 create table userRole.roles
 (
     id   bigint      not null
        primary key,
     name varchar(60) null,
     constraint UK_nb4h0p6txrmfc0xbrd1kglp9t
         unique (name)
 );

 create table userRole.users
 (
     id       bigint auto_increment
         primary key,
     avatar   varchar(255) null,
     email    varchar(50)  null,
     fullname varchar(50)  null,
     password varchar(100) null,
     username varchar(50)  null,
     constraint UK_sx468g52bpetvlad2j9y0lptc
         unique (email),
     constraint users_uk
         unique (email, username)
 );

 create table userRole.users_roles
 (
     user_id bigint not null,
     role_id bigint not null,
     primary key (user_id, role_id),
     constraint FK2o0jvgh89lemvvo17cbqvdxaa
         foreign key (user_id) references userRole.users (id)
             on delete cascade,
     constraint FKj6m8fwv7oqv74fcehir1a9ffy
         foreign key (role_id) references userRole.roles (id)
 );
