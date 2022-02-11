# Oauth2 客户端信息
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`
(
    `client_id`               varchar(48) NOT NULL,
    `resource_ids`            varchar(256)  DEFAULT NULL,
    `client_secret`           varchar(256)  DEFAULT NULL,
    `scope`                   varchar(256)  DEFAULT NULL,
    `authorized_grant_types`  varchar(256)  DEFAULT NULL,
    `web_server_redirect_uri` varchar(256)  DEFAULT NULL,
    `authorities`             varchar(256)  DEFAULT NULL,
    `access_token_validity`   int(11)       DEFAULT NULL,
    `refresh_token_validity`  int(11)       DEFAULT NULL,
    `additional_information`  varchar(4096) DEFAULT NULL,
    `autoapprove`             varchar(256)  DEFAULT NULL,
    PRIMARY KEY (`client_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO `oauth_client_details` VALUES ('client_td123','autodeliver,resume', 'abcxyz', 'all', 'password,refresh_token', NULL, NULL, 7200, 259200, NULL, NULL);


# 用户表
DROP TABLE IF EXISTS `oauth_users`;
CREATE TABLE `oauth_users`
(
    `id`       int(11) NOT NULL AUTO_INCREMENT,
    `username` char(10)  DEFAULT NULL,
    `password` char(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8;

INSERT INTO `oauth_users` VALUES (1, 'admin', '123456');
