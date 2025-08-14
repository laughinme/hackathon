import React from 'react';
import { MapPin, Award } from 'lucide-react';

const BookCard = ({ book, onReserve, onSelect }) => {
  const isReserved = !book.is_available;

  return (
    <div className="flex flex-col rounded-xl overflow-hidden transition-all duration-300 hover:shadow-lg hover:-translate-y-1" style={{ backgroundColor: 'var(--md-sys-color-surface-container-high)' }}>
        <div onClick={onSelect} className="w-full aspect-[3/4] bg-cover bg-center cursor-pointer" style={{ backgroundImage: `url(${book.photo_urls[0] || 'https://placehold.co/400x600/3A342B/E8E2D4?text=No+Image'})`}}></div>
        <div className="p-4 flex flex-col flex-grow">
            <div onClick={onSelect} className="cursor-pointer">
              <span className="text-xs font-medium px-2 py-0.5 rounded" style={{ backgroundColor: 'var(--md-sys-color-tertiary-container)', color: 'var(--md-sys-color-on-tertiary-container)' }}>{book.genre.name}</span>
              <h3 className="font-bold text-lg mt-1 truncate">{book.title}</h3>
              <p className="text-sm truncate" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>{book.author.name}</p>
            </div>

            <div className="space-y-2 text-sm my-3 flex-grow" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
              <p className="flex items-center gap-2"><MapPin size={14} /> <span>{book.exchange_location.title}</span></p>
              <p className="flex items-center gap-2"><Award size={14} /> <span className="capitalize">{book.condition}</span></p>
            </div>

            <button
              onClick={(e) => { e.stopPropagation(); if (!isReserved) onReserve(book.id); }} 
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
    </div>
  );
};

export default BookCard;