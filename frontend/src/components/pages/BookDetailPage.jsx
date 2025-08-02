import React from 'react';
import { ArrowLeft, Star, MapPin, Clock, User, Heart, Share2, Calendar, ShieldCheck } from 'lucide-react';

const BookDetailPage = ({ book, onBack }) => {
  if (!book) return null; 

  const conditionLabels = {
    new: 'Новая',
    good: 'Хорошее',
    fair: 'Удовлетворительное'
  };

  return (
    <div className="p-8">
      <button onClick={onBack} className="flex items-center mb-6 font-semibold" style={{ color: 'var(--md-sys-color-primary)' }}>
        <ArrowLeft size={20} className="mr-2" />
        Назад к каталогу
      </button>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2 space-y-6">
          <div className="p-6 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="aspect-[3/4] rounded-lg bg-cover bg-center" style={{ backgroundImage: `url(${book.image || 'https://placehold.co/400x600/3A342B/E8E2D4?text=No+Image'})`, backgroundColor: 'var(--md-sys-color-surface-variant)' }}></div>
              
              <div className="space-y-4">
                <h1 className="text-3xl font-bold">{book.title}</h1>
                <p className="text-xl text-muted-foreground" style={{color: 'var(--md-sys-color-on-surface-variant)'}}>{book.author}</p>
                <div className="flex items-center gap-4">
                  <span className="flex items-center gap-1"><Star className="h-5 w-5 text-yellow-400" /> {book.rating}</span>
                  <span className="text-sm font-medium px-2 py-1 rounded" style={{ backgroundColor: 'var(--md-sys-color-tertiary-container)', color: 'var(--md-sys-color-on-tertiary-container)' }}>{conditionLabels[book.condition] || book.condition}</span>
                </div>
                <div className="flex flex-wrap gap-2">
                  {book.tags?.map(tag => <span key={tag} className="text-xs px-2 py-1 rounded" style={{backgroundColor: 'var(--md-sys-color-surface-container-high)'}}>{tag}</span>)}
                </div>
                <div className="text-sm pt-4 border-t" style={{borderColor: 'var(--md-sys-color-outline-variant)'}}>
                  <p><strong className="text-muted-foreground">Издательство:</strong> {book.publisher}</p>
                  <p><strong className="text-muted-foreground">Год:</strong> {book.year}</p>
                </div>
              </div>
            </div>
          </div>
          <div className="p-6 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
            <h3 className="text-xl font-bold mb-2">Описание</h3>
            <p className="text-muted-foreground" style={{color: 'var(--md-sys-color-on-surface-variant)'}}>{book.description}</p>
          </div>
        </div>

        <div className="space-y-6">
          <div className="p-6 rounded-xl space-y-4" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
            <h3 className="text-xl font-bold">{book.status === 'available' ? 'Доступна для обмена' : 'Забронирована'}</h3>
            <div className="flex items-center gap-2 text-sm"><MapPin size={16} /> {book.location} - {book.distance}</div>
            <button disabled={book.status !== 'available'} className="w-full py-3 rounded-lg font-semibold" style={{ backgroundColor: book.status === 'available' ? 'var(--md-sys-color-primary)' : 'var(--md-sys-color-surface-variant)', color: book.status === 'available' ? 'var(--md-sys-color-on-primary)' : 'var(--md-sys-color-on-surface-variant)' }}>
              {book.status === 'available' ? 'Забронировать книгу' : 'Недоступна'}
            </button>
          </div>
          <div className="p-6 rounded-xl space-y-4" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
            <h3 className="text-xl font-bold">Владелец</h3>
            <div className="flex items-center gap-3">
              <div className="w-12 h-12 rounded-full bg-cover bg-center" style={{backgroundImage: `url(${book.owner.avatar || 'https://placehold.co/80x80/DBC66E/3A3000?text=A'})`}}></div>
              <div>
                <p className="font-semibold flex items-center gap-2">{book.owner.name} <ShieldCheck className="h-4 w-4 text-green-500" /></p>
                <p className="text-sm flex items-center gap-1"><Star size={14} className="text-yellow-400" /> {book.owner.rating}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BookDetailPage;
