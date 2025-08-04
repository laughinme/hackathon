import React, { useState, useEffect, useContext } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import { AuthContext } from '../../App';
import apiProtected from '../../api/axios';
import { Link } from 'react-router-dom';

delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon-2x.png',
  iconUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-icon.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.7.1/dist/images/marker-shadow.png',
});

const MapPage = () => {
  const { currentUser } = useContext(AuthContext);
  const [locations, setLocations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [mapCenter, setMapCenter] = useState([55.7558, 37.6173]);
  const [zoom, setZoom] = useState(10);

  useEffect(() => {
    const fetchLocations = async () => {
      try {
        const response = await apiProtected.get('/geo/exchange_locations/', {
          params: { filter: false }
        });
        setLocations(response.data);

        if (currentUser?.city && response.data.length > 0) {
          const userCityLocation = response.data.find(loc => loc.city.id === currentUser.city.id);
          if (userCityLocation) {
            setMapCenter([userCityLocation.latitude, userCityLocation.longitude]);
            setZoom(12);
          }
        }
      } catch (err) {
        console.error("Failed to fetch locations:", err);
        setError("Не удалось загрузить точки обмена.");
      } finally {
        setLoading(false);
      }
    };

    fetchLocations();
  }, [currentUser]);

  if (loading) {
    return <div className="p-8 text-center">Загрузка карты...</div>;
  }

  if (error) {
    return <div className="p-8 text-center text-red-400">{error}</div>;
  }

  return (
    // --- ИЗМЕНЕНИЕ ЗДЕСЬ ---
    <div className="absolute inset-0">
      <MapContainer center={mapCenter} zoom={zoom} scrollWheelZoom={true} style={{ height: '100%', width: '100%' }}>
        <TileLayer
          attribution='© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        {locations.map(loc => (
          <Marker key={loc.id} position={[loc.latitude, loc.longitude]}>
            <Popup>
              <div className="p-1">
                <h3 className="font-bold text-base mb-1">{loc.title}</h3>
                <p className="text-sm text-gray-600">{loc.address}</p>
                {loc.opening_hours && <p className="text-xs mt-2">Часы работы: {loc.opening_hours}</p>}
                <a 
                  href={`https://yandex.ru/maps/?rtext=~${loc.latitude},${loc.longitude}`} 
                  target="_blank" 
                  rel="noopener noreferrer"
                  className="text-blue-500 hover:underline text-sm mt-2 block"
                >
                  Проложить маршрут
                </a>
              </div>
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
};

export default MapPage;