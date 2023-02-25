-- if the result set is empty while access the data from the database 
-- please use the below command as some statements are no longer supported 
-- in mysql version 8

SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));
