-- ############## compounds ##############
select * from compounds limit 10;

-- ############## structures ##############
--delete from structures;
select count(*) from structures;

-- ############## comments ##############
--delete from comments;
select count(*) from comments;

-- ############## reference ##############
select count(*) from reference  where id > 8464286;
select * from reference where id = 8464286;
