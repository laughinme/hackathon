from sqlalchemy import func


def dist_expression(
    cls,
    lat: float,
    lon: float,
):
    """
    Calculate distance using the Haversine formula directly in sql
    """
    earth_radius = 6371
    dist_expr = earth_radius * func.acos(
        func.cos(func.radians(lat))
        * func.cos(func.radians(cls.latitude))
        * func.cos(func.radians(cls.longitude) - func.radians(lon))
        + func.sin(func.radians(lat)) * func.sin(func.radians(cls.latitude))
    )
    
    return dist_expr
