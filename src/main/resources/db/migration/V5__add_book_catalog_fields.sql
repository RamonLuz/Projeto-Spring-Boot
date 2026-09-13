alter table books add column isbn varchar(20);
alter table books add column category varchar(80) default 'Geral' not null;
alter table books add column description varchar(500);
alter table books add column stock integer default 0 not null;
alter table books add column featured boolean default false not null;
alter table books add column active boolean default true not null;

alter table books add constraint uk_book_isbn unique (isbn);

create index idx_books_category on books (category);
create index idx_books_active_stock on books (active, stock);
