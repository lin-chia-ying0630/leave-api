
CREATE TABLE leave_request (
    id BIGINT NOT NULL AUTO_INCREMENT,
    applicant VARCHAR(255) NOT NULL,
    leave_type VARCHAR(255),
    start_date DATE,
    end_date DATE,
    reason VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_id VARCHAR(255),
    updated_id VARCHAR(255),
    created_date DATETIME(6),
    updated_date DATETIME(6),
    CONSTRAINT pk_leave_request PRIMARY KEY (id),
    CONSTRAINT chk_leave_request_status
        CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED'))
);

CREATE INDEX idx_leave_request_status ON leave_request (status);
