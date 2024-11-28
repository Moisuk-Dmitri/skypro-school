// TASK 1
alter table student
add constraint age_constraint check (age >= 16 )

// TASK 2
alter table student
alter column name set not null;

alter table student
add constraint name_unique unique (name)


// TASK 3
alter table faculty
add constraint name_color_unique unique (name, color)

// TASK 4
alter table student
alter column name set default 20
