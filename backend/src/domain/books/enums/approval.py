from enum import Enum


class ApprovalStatus(Enum):
    """Supported book approval statuses"""
    PENDING = "pending"
    APPROVED = "approved"
    REJECTED = "rejected"
