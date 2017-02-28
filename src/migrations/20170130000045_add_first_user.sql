insert into poc_users (client_id, first_name, last_name, email_address, phone_number, password, created_at)
values (1, 'adarsh', 'adarsh', 'simpleboy007@yahoo.com', '919880482508', '$2a$10$18IVhB0VNrtb5CQruAbKbep2bV/8WXBeFTVPYvEBpW7HHXTSqGPnO', CURRENT_TIMESTAMP);

insert into poc_user_roles (poc_user_id, role) values(1, 'ADMIN');
insert into poc_user_roles (poc_user_id, role) values(1, 'SUPER');