import React, { useState } from 'react';
import { Filter, ChevronDown, MapPin, Star } from 'lucide-react';

const BookFilters = ({ onFilterChange }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [sort, setSort] = useState('newest');
  const [genre, setGenre] = useState('all');
  const [distance, setDistance] = useState(50);
  const [rating, setRating] = useState(1);

  const handleFilterUpdate = (filterName, value) => {
    const newFilters = { sort, genre, distance, rating, [filterName]: value };
    onFilterChange(newFilters);
  };

  const handleSortChange = (e) => {
    setSort(e.target.value);
    handleFilterUpdate('sort', e.target.value);
  };
  const handleGenreChange = (e) => {
    setGenre(e.target.value);
    handleFilterUpdate('genre', e.target.value);
  };
  const handleDistanceChange = (e) => {
    setDistance(e.target.value);
    handleFilterUpdate('distance', Number(e.target.value));
  };
  const handleRatingChange = (e) => {
    setRating(e.target.value);
    handleFilterUpdate('rating', Number(e.target.value));
  };
  
  const resetFilters = () => {
      setSort('newest');
      setGenre('all');
      setDistance(50);
      setRating(1);
      onFilterChange({ sort: 'newest', genre: 'all', distance: 50, rating: 1 });
  }

  return (
    <div className="rounded-xl mb-6" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
      <div
        className="flex items-center justify-between p-4 cursor-pointer"
        onClick={() => setIsOpen(!isOpen)}
      >
        <div className="flex items-center gap-2 font-semibold">
          <Filter className="h-5 w-5" />
          Фильтры и сортировка
        </div>
        <ChevronDown className={`h-5 w-5 transition-transform ${isOpen ? 'rotate-180' : ''}`} />
      </div>

      {isOpen && (
        <div className="p-4 border-t" style={{ borderColor: 'var(--md-sys-color-outline-variant)' }}>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            <div className="space-y-2">
              <label className="text-sm font-medium">Сортировка</label>
              <select value={sort} onChange={handleSortChange} className="w-full p-2 rounded-lg border bg-transparent focus:outline-none focus:ring-2" style={{ borderColor: 'var(--md-sys-color-outline)', '--tw-ring-color': 'var(--md-sys-color-primary)' }}>
                <option value="newest">Сначала новые</option>
                <option value="distance">По расстоянию</option>
                <option value="rating">По рейтингу</option>
              </select>
            </div>

            <div className="space-y-2">
              <label className="text-sm font-medium">Жанр</label>
              <select value={genre} onChange={handleGenreChange} className="w-full p-2 rounded-lg border bg-transparent focus:outline-none focus:ring-2" style={{ borderColor: 'var(--md-sys-color-outline)', '--tw-ring-color': 'var(--md-sys-color-primary)' }}>
                <option value="all">Все жанры</option>
                <option value="Классика">Классика</option>
                <option value="Фэнтези">Фэнтези</option>
                <option value="Философия">Философия</option>
              </select>
            </div>

            <div className="space-y-2">
              <label className="text-sm font-medium flex items-center gap-2">
                <MapPin className="h-4 w-4" /> Расстояние: до {distance} км
              </label>
              <input type="range" min="1" max="50" value={distance} onChange={handleDistanceChange} className="w-full h-2 rounded-lg appearance-none cursor-pointer" style={{ backgroundColor: 'var(--md-sys-color-surface-variant)' }} />
            </div>

            <div className="space-y-2">
              <label className="text-sm font-medium flex items-center gap-2">
                <Star className="h-4 w-4" /> Рейтинг: от {rating}
              </label>
              <input type="range" min="1" max="5" step="0.1" value={rating} onChange={handleRatingChange} className="w-full h-2 rounded-lg appearance-none cursor-pointer" style={{ backgroundColor: 'var(--md-sys-color-surface-variant)' }} />
            </div>
          </div>
          <div className="flex items-center gap-2 mt-6 pt-4 border-t" style={{ borderColor: 'var(--md-sys-color-outline-variant)' }}>
            <span className="text-sm">Активные фильтры:</span>
            {distance < 50 && <span className="text-xs px-2 py-1 rounded" style={{backgroundColor: 'var(--md-sys-color-secondary-container)'}}>До {distance} км</span>}
            {rating > 1 && <span className="text-xs px-2 py-1 rounded" style={{backgroundColor: 'var(--md-sys-color-secondary-container)'}}>Рейтинг от {rating}</span>}
            <button onClick={resetFilters} className="text-sm ml-auto" style={{color: 'var(--md-sys-color-primary)'}}>Сбросить все</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default BookFilters;
