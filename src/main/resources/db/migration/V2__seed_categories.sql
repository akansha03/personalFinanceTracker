-- V2__seed_categories.sql
-- Insert default Income categories
INSERT INTO categories (name, type) VALUES
('ALLOWANCE', 'Income'),
('SALARY', 'Income'),
('PETTY_CASH', 'Income'),
('BONUS', 'Income'),
('OTHER', 'Income'),
('RENTAL_INCOME', 'Income')
ON CONFLICT DO NOTHING;

-- Insert default Expense categories
INSERT INTO categories (name, type) VALUES
('FOOD', 'Expense'),
('SOCIAL_LIFE', 'Expense'),
('PETS', 'Expense'),
('TRANSPORT', 'Expense'),
('CULTURE', 'Expense'),
('HOUSEHOLD', 'Expense'),
('APPAREL', 'Expense'),
('BEAUTY', 'Expense'),
('HEALTH', 'Expense'),
('EDUCATION', 'Expense'),
('GIFT', 'Expense'),
('OTHER', 'Expense')
ON CONFLICT DO NOTHING;