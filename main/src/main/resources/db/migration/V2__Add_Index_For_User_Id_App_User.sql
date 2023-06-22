CREATE INDEX application_user_telegram_user_id_key
    on application_user (telegram_user_id);
CREATE INDEX binary_content_file_id_key
    on binary_content (file_id);
CREATE INDEX binary_content_type_file_key
    on binary_content (type_file);
