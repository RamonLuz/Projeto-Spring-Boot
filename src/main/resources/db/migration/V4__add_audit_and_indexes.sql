alter table books add column created_at timestamp default current_timestamp not null;
alter table books add column updated_at timestamp default current_timestamp not null;

alter table customers add column created_at timestamp default current_timestamp not null;
alter table customers add column updated_at timestamp default current_timestamp not null;

alter table employees add column created_at timestamp default current_timestamp not null;
alter table employees add column updated_at timestamp default current_timestamp not null;

alter table sales add column created_at timestamp default current_timestamp not null;
alter table sales add column updated_at timestamp default current_timestamp not null;

alter table app_users add column created_at timestamp default current_timestamp not null;
alter table app_users add column updated_at timestamp default current_timestamp not null;

create index idx_books_title on books (title);
create index idx_books_author on books (author);
create index idx_customers_status on customers (status);
create index idx_employees_status on employees (status);
create index idx_sales_sale_date on sales (sale_date);
create index idx_sales_customer_id on sales (customer_id);
create index idx_sales_employee_id on sales (employee_id);
create index idx_sales_book_id on sales (book_id);
create index idx_app_users_username on app_users (username);
