-- spring-security-oauth官方的表结地址：https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/test/resources/schema.sql
-- 但是，官方的数据库脚本是微软的access数据库，修改成mysql适用的数据库脚本需要做如下改动：
-- 对于MySQL来说，默认建表语句中主键是varchar(255)类型，在mysql中执行会报错，原因是mysql对varchar主键长度有限制。所以这里改成255即可。
-- 其次，语句中会有某些字段为LONGVARBINARY类型，它对应mysql的blob类型，也需要修改一下。
-- 下面的脚本是修改过后的脚本

-- 数据库中各个表和字段说明：http://andaily.com/spring-oauth-server/db_table_description.html

-- used in tests that use HSQL
DROP TABLE IF EXISTS oauth_client_details;
create table oauth_client_details (
  client_id VARCHAR(255) PRIMARY KEY,
  resource_ids VARCHAR(255),
  client_secret VARCHAR(255),
  scope VARCHAR(255),
  authorized_grant_types VARCHAR(255),
  web_server_redirect_uri VARCHAR(255),
  authorities VARCHAR(255),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(255)
);

DROP TABLE IF EXISTS oauth_client_token;
create table oauth_client_token (
  token_id VARCHAR(255),
  token blob,
  authentication_id VARCHAR(255) PRIMARY KEY,
  user_name VARCHAR(255),
  client_id VARCHAR(255)
);

DROP TABLE IF EXISTS oauth_access_token;
create table oauth_access_token (
  token_id VARCHAR(255),
  token blob,
  authentication_id VARCHAR(255) PRIMARY KEY,
  user_name VARCHAR(255),
  client_id VARCHAR(255),
  authentication blob,
  refresh_token VARCHAR(255)
);

DROP TABLE IF EXISTS oauth_refresh_token;
create table oauth_refresh_token (
  token_id VARCHAR(255),
  token blob,
  authentication blob
);

DROP TABLE IF EXISTS oauth_code;
create table oauth_code (
  code VARCHAR(255), authentication blob
);

DROP TABLE IF EXISTS oauth_approvals;
create table oauth_approvals (
	userId VARCHAR(255),
	clientId VARCHAR(255),
	scope VARCHAR(255),
	status VARCHAR(10),
	expiresAt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	lastModifiedAt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间'
);


-- customized oauth_client_details table
-- create table ClientDetails (
--   appId VARCHAR(255) PRIMARY KEY,
--   resourceIds VARCHAR(255),
--   appSecret VARCHAR(255),
--   scope VARCHAR(255),
--   grantTypes VARCHAR(255),
--   redirectUrl VARCHAR(255),
--   authorities VARCHAR(255),
--   access_token_validity INTEGER,
--   refresh_token_validity INTEGER,
--   additionalInformation VARCHAR(255),
--   autoApproveScopes VARCHAR(255)
-- );

-- 插入初始化的数据
insert into `oauth_client_details`
( `client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`)
values
( 'clientid', null, 'secret', 'read,write', 'password,refresh_token', '', '', null, null, '{}', '');

-- 这样的话可以通过如下请求获取token信息
-- curl -X POST -u clientid:secret -d "grant_type=password&username=admin&password=admin" 'http://localhost:10000/oauth2/oauth/token'