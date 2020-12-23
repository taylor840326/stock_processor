CREATE TABLE `t_daily` (
  `id` bigint not null auto_increment primary key,
  `amount` float DEFAULT NULL,
  `change_price` float DEFAULT NULL,
  `close_price` float DEFAULT NULL,
  `high_price` float DEFAULT NULL,
  `low_price` float DEFAULT NULL,
  `open_price` float DEFAULT NULL,
  `pct_chg` float DEFAULT NULL,
  `pre_close` float DEFAULT NULL,
  `trade_date` varchar(255) DEFAULT NULL,
  `ts_code` varchar(255) DEFAULT NULL,
  `vol` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8