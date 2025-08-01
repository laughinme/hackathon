from enum import Enum


class Condition(Enum):
    """Supported book conditions"""
    NEW = "new"
    PERFECT = "perfect"
    GOOD = "good"
    NORMAL = "normal"
