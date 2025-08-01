import React from 'react';
import { Star, MapPin, Award, Clock } from 'lucide-react';

// Добавлен проп onSelect
const BookCard = ({ book, onReserve, onSelect }) => {
  const isReserved = book.status === 'reserved';

  return (
    // Добавлен курсор и обработчик клика
    <div onClick={onSelect} className="p-4 rounded-xl flex flex-col cursor-pointer transition-all hover:scale-[1.02] hover:shadow-lg" style={{ backgroundColor: 'var(--md-sys-color-surface-container-high)' }}>
      {/* Верхняя часть */}
      <div className="flex mb-4">
        <div className="w-24 h-36 rounded-lg flex-shrink-0 mr-4 bg-cover bg-center" style={{ backgroundImage: `url(${book.image || 'https://placehold.co/200x300/3A342B/E8E2D4?text=No+Image'})`, backgroundColor: 'var(--md-sys-color-surface-variant)' }}></div>
        <div>
          <h3 className="font-bold text-lg">{book.title}</h3>
          <p className="text-sm" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>{book.author}</p>
          <div className="flex items-center mt-2 space-x-4">
            <span className="flex items-center text-sm"><Star size={14} className="mr-1 text-yellow-400" /> {book.rating}</span>
            <span className="text-sm font-medium px-2 py-0.5 rounded" style={{ backgroundColor: 'var(--md-sys-color-tertiary-container)', color: 'var(--md-sys-color-on-tertiary-container)' }}>{book.genre}</span>
          </div>
        </div>
      </div>

      {/* Детали */}
      <div className="space-y-2 text-sm mb-4" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
        <p className="flex items-center"><MapPin size={14} className="mr-2" /> {book.location} - {book.distance}</p>
        <p className="flex items-center"><Award size={14} className="mr-2" /> {book.condition} от {book.owner.name}</p>
        <p className="flex items-center"><Clock size={14} className="mr-2" /> {book.added}</p>
      </div>

      {/* Кнопка действия */}
      <button
        onClick={(e) => { e.stopPropagation(); if (!isReserved) onReserve(book.id); }} // Останавливаем всплытие, чтобы не сработал onSelect
        disabled={isReserved}
        className={`w-full mt-auto py-2 rounded-lg font-semibold transition-colors ${isReserved ? 'cursor-not-allowed' : ''}`}
        style={{
          backgroundColor: isReserved ? 'var(--md-sys-color-surface-variant)' : 'var(--md-sys-color-primary)',
          color: isReserved ? 'var(--md-sys-color-on-surface-variant)' : 'var(--md-sys-color-on-primary)',
        }}
      >
        {isReserved ? 'Забронирована' : 'Забронировать'}
      </button>
    </div>
  );
};

export default BookCard;
