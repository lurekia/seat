-- 修改seat表的state字段类型为VARCHAR(20)
ALTER TABLE seat MODIFY COLUMN state VARCHAR(20);

-- 添加开始时间和结束时间字段
ALTER TABLE seat ADD COLUMN start_time VARCHAR(50);
ALTER TABLE seat ADD COLUMN end_time VARCHAR(50);

-- 更新现有数据
UPDATE seat SET state = 'AVAILABLE' WHERE state = '否';
UPDATE seat SET state = 'IN_USE' WHERE state = '是'; 