-- 清空现有数据
DELETE FROM test_table;

-- 重置自增ID
ALTER TABLE test_table AUTO_INCREMENT = 1;

-- 插入50条测试数据
INSERT INTO test_table (name, create_time) VALUES
('测试数据_01', NOW()),
('测试数据_02', NOW()),
('测试数据_03', NOW()),
('测试数据_04', NOW()),
('测试数据_05', NOW()),
('测试数据_06', NOW()),
('测试数据_07', NOW()),
('测试数据_08', NOW()),
('测试数据_09', NOW()),
('测试数据_10', NOW()),
('测试数据_11', NOW()),
('测试数据_12', NOW()),
('测试数据_13', NOW()),
('测试数据_14', NOW()),
('测试数据_15', NOW()),
('测试数据_16', NOW()),
('测试数据_17', NOW()),
('测试数据_18', NOW()),
('测试数据_19', NOW()),
('测试数据_20', NOW()),
('测试数据_21', NOW()),
('测试数据_22', NOW()),
('测试数据_23', NOW()),
('测试数据_24', NOW()),
('测试数据_25', NOW()),
('测试数据_26', NOW()),
('测试数据_27', NOW()),
('测试数据_28', NOW()),
('测试数据_29', NOW()),
('测试数据_30', NOW()),
('测试数据_31', NOW()),
('测试数据_32', NOW()),
('测试数据_33', NOW()),
('测试数据_34', NOW()),
('测试数据_35', NOW()),
('测试数据_36', NOW()),
('测试数据_37', NOW()),
('测试数据_38', NOW()),
('测试数据_39', NOW()),
('测试数据_40', NOW()),
('测试数据_41', NOW()),
('测试数据_42', NOW()),
('测试数据_43', NOW()),
('测试数据_44', NOW()),
('测试数据_45', NOW()),
('测试数据_46', NOW()),
('测试数据_47', NOW()),
('测试数据_48', NOW()),
('测试数据_49', NOW()),
('测试数据_50', NOW()); 