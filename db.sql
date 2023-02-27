CREATE TABLE `ndc_am_matrix` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`contractid` bigint NOT NULL,
	`userid` bigint NOT NULL,
	`issequential` tinyint(1) NOT NULL,
	`softdelete` tinyint(1) NOT NULL DEFAULT '0',
	`accountNo` varchar(255) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `ndc_am_matrix_detail` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`approvalmasterid` bigint NOT NULL,
	`approverid` bigint NOT NULL,
	`sequenceno` int NOT NULL,
	`approvalorder` int NOT NULL,
	`role` varchar(255) NOT NULL,
	`groupno` int NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `ndc_am_state` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`state` varchar(255) NOT NULL,
	`softdelete` tinyint(1) NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`)
);

CREATE TABLE `ndc_am_request` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`approvalmasterid` bigint NOT NULL,
	`requesterid` bigint NOT NULL,
	`createdate` TIMESTAMP NOT NULL,
	`status` varchar(255) NOT NULL,
	`modifydate` TIMESTAMP NOT NULL,
	`modifyby` varchar(255) NOT NULL,
	`remarks` varchar(255) NOT NULL,
	`referenceno` varchar(255) NOT NULL,
	`featureactionid` varchar(255) NOT NULL,
	`softdelete` tinyint(1) NOT NULL DEFAULT '0',
	`contractid` bigint NOT NULL,
	`accountNo` varchar(255) NOT NULL,
	`issequential` tinyint(1) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `ndc_am_instances` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`requestid` bigint NOT NULL,
	`approverid` bigint NOT NULL,
	`sequenceno` int NOT NULL,
	`approverorder` int NOT NULL,
	`assigndate` TIMESTAMP NOT NULL,
	`status` varchar(255) NOT NULL,
	`remarks` varchar(255) NOT NULL,
	`rulevalue` int NOT NULL,
	`groupno` int NOT NULL,
	`seqstatus` varchar(50) NOT NULL,
	`groupstatus` varchar(50) NOT NULL,
	`approvedate` TIMESTAMP NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `ndc_sequencerule` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`approvalmasterid` bigint NOT NULL,
	`sequenceno` int NOT NULL,
	`rulevalue` int NOT NULL,
	`rule` varchar(255) NOT NULL,
	`groupno` int NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `ndc_am_featureactionconfig` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`featureactionid` varchar(255) NOT NULL,
	`name` varchar(255) NOT NULL,
	`contractid` bigint NOT NULL,
	`createby` varchar(255) NOT NULL,
	`createdate` TIMESTAMP NOT NULL,
	`modifyby` varchar(255) NOT NULL,
	`modifydate` TIMESTAMP NOT NULL,
	`isenabled` tinyint(1) NOT NULL DEFAULT '1',
	`accountNo` varchar(255) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `ndc_am_approvalmatrixfeatureaction` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`featureid` varchar(255) NOT NULL,
	`featureactionid` varchar(255) NOT NULL,
	`name` varchar(255) NOT NULL,
	`description` varchar(255) NOT NULL,
	`isenabled` tinyint(1) NOT NULL DEFAULT '1',
	PRIMARY KEY (`id`)
);

CREATE TABLE `ndc_am_workflow` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`approvalmasterid` bigint NOT NULL,
	`workflowid` bigint NOT NULL,
	`issequential` tinyint NOT NULL,
	`featureactionid` varchar(255) NOT NULL,
	`minamount` DECIMAL(19,2) NOT NULL,
	`maxamoount` DECIMAL(19,2) NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE `ndc_am_matrix_detail1` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`matrixid` bigint NOT NULL,
	`workflowid` bigint NOT NULL,
	`sequenceno` int NOT NULL,
	`groupno` int NOT NULL,
	`role` varchar(255) NOT NULL,
	`ischecker` tinyint(1) NOT NULL DEFAULT '0',
	`rulevalue` int NOT NULL,
	`rule` varchar(255) NOT NULL,
	PRIMARY KEY (`id`)
);

ALTER TABLE `ndc_am_matrix_detail` ADD CONSTRAINT `ndc_am_matrix_detail_fk0` FOREIGN KEY (`approvalmasterid`) REFERENCES `ndc_am_matrix`(`id`);

ALTER TABLE `ndc_am_request` ADD CONSTRAINT `ndc_am_request_fk0` FOREIGN KEY (`approvalmasterid`) REFERENCES `ndc_am_matrix`(`id`);

ALTER TABLE `ndc_am_request` ADD CONSTRAINT `ndc_am_request_fk1` FOREIGN KEY (`status`) REFERENCES `ndc_am_state`(`state`);

ALTER TABLE `ndc_am_instances` ADD CONSTRAINT `ndc_am_instances_fk0` FOREIGN KEY (`requestid`) REFERENCES `ndc_am_request`(`id`);

ALTER TABLE `ndc_am_instances` ADD CONSTRAINT `ndc_am_instances_fk1` FOREIGN KEY (`status`) REFERENCES `ndc_am_state`(`state`);

ALTER TABLE `ndc_sequencerule` ADD CONSTRAINT `ndc_sequencerule_fk0` FOREIGN KEY (`approvalmasterid`) REFERENCES `ndc_am_matrix`(`id`);

ALTER TABLE `ndc_am_workflow` ADD CONSTRAINT `ndc_am_workflow_fk0` FOREIGN KEY (`approvalmasterid`) REFERENCES `ndc_am_matrix`(`id`);

ALTER TABLE `ndc_am_matrix_detail1` ADD CONSTRAINT `ndc_am_matrix_detail1_fk0` FOREIGN KEY (`matrixid`) REFERENCES `ndc_am_matrix`(`id`);










