package com.br.fullPvp.mysql;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SqlQuerys {
	
	ACCOUNT_CREATE_TABLE("CREATE TABLE IF NOT EXISTS `fullpvp_accounts` (`uniqueid` VARCHAR(60) NOT NULL, `nickname` VARCHAR(16) NOT NULL, `rankname` VARCHAR(32), `groupname` VARCHAR(32), `lastgroupname` VARCHAR(32), `clanname` VARCHAR(32), `address` VARCHAR(32), `lastaddress` VARCHAR(32), `tagusing` VARCHAR(32), `timegroup` LONG, `firstlogin` LONG, `lastsee` LONG, `real` DOUBLE, `cash` DOUBLE, `reputation` DOUBLE, `tags` VARCHAR(1000), `permissions` VARCHAR(1000));"),
	ACCOUNT_SELECT("SELECT * FROM `fullpvp_accounts` WHERE `uniqueid`=?"),
	ACCOUNT_SELECT_ALL("SELECT * FROM `fullpvp_accounts`;"),
	ACCOUNT_INSERT("INSERT INTO `fullpvp_accounts` (`uniqueid`,`nickname`,`rankname`,`groupname`,`lastgroupname`,`clanname`,`address`,`lastaddress`,`tagusing`,`timegroup`,`firstlogin`,`lastsee`,`real`,`cash`,`reputation`,`tags`,`permissions`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);"),
	ACCOUNT_UPDATE("UPDATE `fullpvp_accounts` SET `nickname`=?,`rankname`=?,`groupname`=?,`lastgroupname`=?,`clanname`=?,`address`=?,`lastaddress`=?,`tagusing`=?,`timegroup`=?,`lastsee`=?,`real`=?,`cash`=?,`reputation`=?,`tags`=?,`permissions`=? WHERE `uniqueid`=?"),
	
	GROUP_CREATE_TABLE("CREATE TABLE IF NOT EXISTS `fullpvp_groups` (`name` VARCHAR(32), `prefix` VARCHAR(32), `priority` INT, `staff` BOOLEAN, `defaulted` BOOLEAN, `permissions` VARCHAR(1000));"),
	GROUP_SELECT("SELECT * FROM `fullpvp_groups` WHERE `name`=?"),
	GROUP_SELECT_ALL("SELECT * FROM `fullpvp_groups`;"),
	GROUP_INSERT("INSERT INTO `fullpvp_groups` (`name`,`prefix`,`priority`,`staff`,`defaulted`,`permissions`) VALUES (?,?,?,?,?,?);"),
	GROUP_UPDATE("UPDATE `fullpvp_groups` SET `prefix`=?,`priority`=?,`staff`=?,`defaulted`=?,`permissions`=? WHERE `name`=?"),
	GROUP_DELETE("DELETE FROM `fullpvp_groups` WHERE `name`=?"),
	
	STATUS_CREATE_TABLE("CREATE TABLE IF NOT EXISTS `fullpvp_status` (`uniqueid` VARCHAR(60), `kills` DOUBLE, `deaths` DOUBLE);"),
	STATUS_SELECT("SELECT * FROM `fullpvp_status` WHERE `uniqueid`=?"),
	STATUS_SELECT_ALL("SELECT * FROM `fullpvp_status`;"),
	STATUS_INSERT("INSERT INTO `fullpvp_status` (`uniqueid`,`kills`,`deaths`) VALUES (?,?,?);"),
	STATUS_UPDATE("UPDATE `fullpvp_status` SET `kills`=?,`deaths`=? WHERE `uniqueid`=?"),
	
	PREFERENCES_CREATE_TABLE("CREATE TABLE IF NOT EXISTS `fullpvp_preferences` (`uniqueid` VARCHAR(60), `adminmode` BOOLEAN, `staffchat` BOOLEAN, `receivetell` BOOLEAN, `receivedonate` BOOLEAN, `receivereports` BOOLEAN, `receivewarns` BOOLEAN, `fly` BOOLEAN, `scoreboard` BOOLEAN, `requestsclan` BOOLEAN, `requeststpa` BOOLEAN);"),
	PREFERENCES_SELECT("SELECT * FROM `fullpvp_preferences` WHERE `uniqueid`=?"),
	PREFERENCES_INSERT("INSERT INTO `fullpvp_preferences` (`uniqueid`, `adminmode`, `staffchat`, `receivetell`, `receivedonate`, `receivereports`, `receivewarns`, `fly`, `scoreboard`, `requestsclan`, `requeststpa`) VALUES (?,?,?,?,?,?,?,?,?,?,?);"),
	PREFERENCES_UPDATE("UPDATE `fullpvp_preferences` SET `adminmode`=?,`staffchat`=?,`receivetell`=?,`receivedonate`=?,`receivereports`=?,`receivewarns`=?,`fly`=?,`scoreboard`=?,`requestsclan`=?,`requeststpa`=? WHERE `uniqueid`=?"),
	
	RANK_CREATE_TABLE("CREATE TABLE IF NOT EXISTS `fullpvp_ranks` (`id` INT, `name` VARCHAR(32), `prefix` VARCHAR(32), `priority` INT, `defaulted` BOOLEAN, `requirements` VARCHAR(1000));"),
	RANK_SELECT("SELECT * FROM `fullpvp_ranks` WHERE `name`=?"),
	RANK_SELECT_ALL("SELECT * FROM `fullpvp_ranks`;"),
	RANK_INSERT("INSERT INTO `fullpvp_ranks` (`id`,`name`,`prefix`,`priority`,`defaulted`,`requirements`) VALUES (?,?,?,?,?,?);"),
	RANK_UPDATE("UPDATE `fullpvp_ranks` SET `prefix`=?,`priority`=?,`defaulted`=?,`requirements`=? WHERE `name`=?"),
	RANK_DELETE("DELETE FROM `fullpvp_ranks` WHERE `name`=?"),
	
	LINK_CREATE_TABLE("CREATE TABLE IF NOT EXISTS `fullpvp_links` (`id` INT, `prefix` VARCHAR(64), `website` VARCHAR(64), `shop` VARCHAR(64), `discord` VARCHAR(64), `address` VARCHAR(64), `youtubechannel` VARCHAR(64), `twitter` VARCHAR(64), `motd` VARCHAR(1000));"),
	LINK_SELECT("SELECT * FROM `fullpvp_links` WHERE `id`=?"),
	LINK_INSERT("INSERT INTO `fullpvp_links` (`id`,`prefix`,`website`,`shop`,`discord`,`address`,`youtubechannel`,`twitter`,`motd`) VALUES (?,?,?,?,?,?,?,?,?);"),
	LINK_UPDATE("UPDATE `fullpvp_links` SET `prefix`=?,`website`=?,`shop`=?,`discord`=?,`address`=?,`youtubechannel`=?,`twitter`=?,`motd`=? WHERE `id`=?"),
	
	WARP_CREATE_TABLE("CREATE TABLE IF NOT EXISTS `fullpvp_warps` (`name` VARCHAR(32), `permission` VARCHAR(32), `location` VARCHAR(300), `closed` BOOLEAN, `exclusive` BOOLEAN, `fly` BOOLEAN, `pvp` BOOLEAN, `icon` VARCHAR(16), `blockedcommands` VARCHAR(1000));"),
	WARP_SELECT("SELECT * FROM `fullpvp_warps` WHERE `name`=?"),
	WARP_SELECT_ALL("SELECT * FROM `fullpvp_warps`;"),
	WARP_INSERT("INSERT INTO `fullpvp_warps` (`name`,`permission`,`location`,`closed`,`exclusive`,`fly`,`pvp`,`icon`,`blockedcommands`) VALUES (?,?,?,?,?,?,?,?,?);"),
	WARP_UPDATE("UPDATE `fullpvp_warps` SET `permission`=?,`location`=?,`closed`=?,`exclusive`=?,`fly`=?,`pvp`=?,`icon`=?,`blockedcommands`=? WHERE `name`=?"),
	WARP_DELETE("DELETE FROM `fullpvp_warps` WHERE `name`=?"),
	
	TAG_CREATE_TABLE("CREATE TABLE IF NOT EXISTS `fullpvp_tags` (`name` VARCHAR(32), `prefix` VARCHAR(32), `permission` VARCHAR(300), `free` BOOLEAN);"),
	TAG_SELECT("SELECT * FROM `fullpvp_tags` WHERE `name`=?"),
	TAG_SELECT_ALL("SELECT * FROM `fullpvp_tags`;"),
	TAG_INSERT("INSERT INTO `fullpvp_tags` (`name`,`prefix`,`permission`,`free`) VALUES (?,?,?,?);"),
	TAG_UPDATE("UPDATE `fullpvp_tags` SET `prefix`=?,`permission`=?,`free`=? WHERE `name`=?"),
	TAG_DELETE("DELETE FROM `fullpvp_tags` WHERE `name`=?"),
	
	PREFERENCES_SERVER_CREATE_TABLE("CREATE TABLE IF NOT EXISTS `fullpvp_preferences_server` (`id` INT, `chatlocal` BOOLEAN, `chatglobal` BOOLEAN, `manutence` BOOLEAN);"),
	PREFERENCES_SERVER_SELECT("SELECT * FROM `fullpvp_preferences_server` WHERE `id`=?"),
	PREFERENCES_SERVER_INSERT("INSERT INTO `fullpvp_preferences_server` (`id`,`chatlocal`,`chatglobal`,`manutence`) VALUES (?,?,?,?);"),
	PREFERENCES_SERVER_UPDATE("UPDATE `fullpvp_preferences_server` SET `chatlocal`=?,`chatglobal`=?,`manutence`=? WHERE `id`=?"),
	
	CLAN_CREATE_TABLE("CREATE TABLE IF NOT EXISTS `fullpvp_clans` (`name` VARCHAR(32), `tag` VARCHAR(32), `leadder` VARCHAR(32), `motto` VARCHAR(1000), `members` VARCHAR(1000), `climbed` VARCHAR(1000), `invites` VARCHAR(1000), `allies` VARCHAR(1000), `enemies` VARCHAR(1000), `real` DOUBLE, `createdin` LONG, `pvp` BOOLEAN);"),
	CLAN_SELECT("SELECT * FROM `fullpvp_clans` WHERE `name`=?"),
	CLAN_SELECT_ALL("SELECT * FROM `fullpvp_clans`;"),
	CLAN_INSERT("INSERT INTO `fullpvp_clans` (`name`,`tag`,`leadder`,`motto`,`members`,`climbed`,`invites`,`allies`,`enemies`,`real`,`createdin`,`pvp`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);"),
	CLAN_UPDATE("UPDATE `fullpvp_clans` SET `tag`=?,`motto`=?,`members`=?,`climbed`=?,`invites`=?,`allies`=?,`enemies`=?,`real`=?,`createdin`=?,`pvp`=? WHERE `name`=?"),
	CLAN_DELETE("DELETE FROM `fullpvp_clans` WHERE `name`=?"),
	
	CLAN_STATUS_CREATE_TABLE("CREATE TABLE IF NOT EXISTS `fullpvp_clans_status` (`clanname` VARCHAR(32), `kills` DOUBLE, `deaths` DOUBLE, `killstreak` DOUBLE);"),
	CLAN_STATUS_SELECT("SELECT * FROM `fullpvp_clans_status` WHERE `clanname`=?"),
	CLAN_STATUS_SELECT_ALL("SELECT * FROM `fullpvp_clans_status`;"),
	CLAN_STATUS_INSERT("INSERT INTO `fullpvp_clans_status` (`clanname`,`kills`,`deaths`,`killstreak`) VALUES (?,?,?,?);"),
	CLAN_STATUS_UPDATE("UPDATE `fullpvp_clans_status` SET `kills`=?,`deaths`=?,`killstreak`=? WHERE `clanname`=?"),
	CLAN_STATUS_DELETE("DELETE FROM `fullpvp_clans_status` WHERE `clanname`=?"),
	
	SESSION_CREATE_TABLE("CREATE TABLE IF NOT EXISTS `fullpvp_shop_sessions` (`name` VARCHAR(32), `descount` DOUBLE, `icon` VARCHAR(16), `promo` BOOLEAN);"),
	SESSION_SELECT("SELECT * FROM `fullpvp_shop_sessions` WHERE `name`=?"),
	SESSION_SELECT_ALL("SELECT * FROM `fullpvp_shop_sessions`;"),
	SESSION_INSERT("INSERT INTO `fullpvp_shop_sessions` (`name`,`descount`,`icon`,`promo`) VALUES (?,?,?,?);"),
	SESSION_UPDATE("UPDATE `fullpvp_shop_sessions` SET `descount`=?,`icon`=?,`promo`=? WHERE `name`=?"),
	SESSION_DELETE("DELETE FROM `fullpvp_shop_sessions` WHERE `name`=?"),
	
	ITEM_CREATE_TABLE("CREATE TABLE IF NOT EXISTS `fullpvp_shop_itens` (`uniqueid` VARCHAR(32), `item` VARCHAR(8000), `session` VARCHAR(32), `price` DOUBLE, `descount` DOUBLE, `promo` BOOLEAN);"),
	ITEM_SELECT("SELECT * FROM `fullpvp_shop_itens` WHERE `uniqueid`=?"),
	ITEM_SELECT_ALL("SELECT * FROM `fullpvp_shop_itens`;"),
	ITEM_INSERT("INSERT INTO `fullpvp_shop_itens` (`uniqueid`,`item`,`session`,`price`,`descount`,`promo`) VALUES (?,?,?,?,?,?);"),
	ITEM_UPDATE("UPDATE `fullpvp_shop_itens` SET `session`=?,`price`=?,`descount`=?,`promo`=? WHERE `uniqueid`=?"),
	ITEM_DELETE("DELETE FROM `fullpvp_shop_itens` WHERE `uniqueid`=?"),
	
	SHOPPING_CART_CREATE_TABLE("CREATE TABLE IF NOT EXISTS `fullpvp_shopping_cart` (`uniqueid` VARCHAR(64), `cart` VARCHAR(8000));"),
	SHOPPING_CART_SELECT("SELECT * FROM `fullpvp_shopping_cart` WHERE `uniqueid`=?"),
	SHOPPING_CART_SELECT_ALL("SELECT * FROM `fullpvp_shopping_cart`;"),
	SHOPPING_CART_INSERT("INSERT INTO `fullpvp_shopping_cart` (`uniqueid`,`cart`) VALUES (?,?);"),
	SHOPPING_CART_UPDATE("UPDATE `fullpvp_shopping_cart` SET `cart`=? WHERE `uniqueid`=?"),
	SHOPPING_CART_DELETE("DELETE FROM `fullpvp_shopping_cart` WHERE `uniqueid`=?"),
	
	KIT_CREATE_TABLE("CREATE TABLE IF NOT EXISTS `fullpvp_kits` (`name` VARCHAR(32), `permission` VARCHAR(32), `timecustom` VARCHAR(32), `icon` VARCHAR(16), `delay` VARCHAR(16), `free` BOOLEAN, `itens` VARCHAR(8000));"),
	KIT_SELECT("SELECT * FROM `fullpvp_kits` WHERE `name`=?"),
	KIT_SELECT_ALL("SELECT * FROM `fullpvp_kits`;"),
	KIT_INSERT("INSERT INTO `fullpvp_kits` (`name`,`permission`,`timecustom`,`icon`,`delay`,`free`,`itens`) VALUES (?,?,?,?,?,?,?);"),
	KIT_UPDATE("UPDATE `fullpvp_kits` SET `permission`=?,`timecustom`=?,`icon`=?,`delay`=?,`free`=?,`itens`=? WHERE `name`=?"),
	KIT_DELETE("DELETE FROM `fullpvp_kits` WHERE `name`=?"),
	
	PLAYER_KIT_CREATE_TABLE("CREATE TABLE IF NOT EXISTS `fullpvp_player_kits` (`uniqueid` VARCHAR(64), `kitcollected` VARCHAR(8000));"),
	PLAYER_KIT_SELECT("SELECT * FROM `fullpvp_player_kits` WHERE `uniqueid`=?"),
	PLAYER_KIT_SELECT_ALL("SELECT * FROM `fullpvp_player_kits`;"),
	PLAYER_KIT_INSERT("INSERT INTO `fullpvp_player_kits` (`uniqueid`,`kitcollected`) VALUES (?,?);"),
	PLAYER_KIT_UPDATE("UPDATE `fullpvp_player_kits` SET `kitcollected`=? WHERE `uniqueid`=?"),
	PLAYER_KIT_DELETE("DELETE FROM `fullpvp_player_kits` WHERE `uniqueid`=?"),
	;
	
	String query;
}
