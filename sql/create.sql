CREATE TABLE `weakness` (
  `id` int NOT NULL,
  `name` varchar(1000) DEFAULT NULL,
  `abstraction` varchar(20) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `description` text CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `likelihood_of_exploit` varchar (50) DEFAULT NULL,
  `language` varchar(50) DEFAULT NULL,
  `version` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `view_member` (
  `base_view_id` int NOT NULL,
  `target_cwe_id` int NOT NULL,
  `view_id` int NOT NULL,
  `type` varchar(20) DEFAULT NULL,
  `version` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `view` (
  `id` int NOT NULL,
  `name` varchar(1000) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `objective` text CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `version` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `related_weakness` (
  `base_weakness_id` int NOT NULL,
  `target_cwe_id` int NOT NULL,
  `nature` varchar(20) NOT NULL,
  `view_id` int NOT NULL,
  `ordinal` varchar(20) DEFAULT NULL,
  `version` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `category_relationship` (
  `category_id` int NOT NULL,
  `target_cwe_id` int NOT NULL,
  `view_id` int NOT NULL,
  `type` varchar(20) DEFAULT NULL,
  `version` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `category` (
  `id` int DEFAULT NULL,
  `name` varchar(1000) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `summary` text CHARACTER SET latin1 COLLATE latin1_swedish_ci,
  `version` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

