create table tb_agent
(
    code         varchar(6) not null primary key,
    name         varchar(40),
    working_area varchar(45),
    commission   numeric(10, 2),
    phone_no     varchar(15),
    country      varchar(25)
);

create table tb_customer
(
    code            varchar(6)     not null primary key,
    name            varchar(40)    not null,
    city            varchar(35),
    working_area    varchar(35)    not null,
    country         varchar(20)    not null,
    grade           int,
    opening_amt     numeric(12, 2) not null,
    receive_amt     numeric(12, 2) not null,
    payment_amt     numeric(12, 2) not null,
    outstanding_amt numeric(12, 2) not null,
    phone_no        varchar(17)    not null,
    agent_code      varchar(6)     not null references tb_agent
);

create table tb_order
(
    number         int            not null primary key,
    amount         numeric(12, 2) not null,
    advance_amount numeric(12, 2) not null,
    order_date     date           not null,
    customer_code  varchar(6)     not null references tb_customer,
    agent_code     varchar(6)     not null references tb_agent,
    description    varchar(60)    not null
);