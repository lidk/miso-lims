ALTER TABLE Project ADD COLUMN `targetedSequencingId` BIGINT (20) DEFAULT NULL AFTER referenceGenomeId;

ALTER TABLE Project ADD FOREIGN KEY (targetedSequencingId) REFERENCES TargetedSequencing (targetedSequencingId);
