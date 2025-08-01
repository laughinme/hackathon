import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ArrowLeft, Star, Calendar, MapPin, Edit, ShieldCheck, Trophy, Book, Users, Clock } from 'lucide-react';
import BookCard from '../common/BookCard';


const MOCK_USER_DATA = {
  id: '1',
  name: 'Анна Козлова',
  avatar: 'https://placehold.co/120x120/DBC66E/3A3000?text=A',
  verified: true,
  rating: 4.9,
  reviewsCount: 127,
  memberSince: '2022-03-15',
  location: 'Москва, Россия',
  bio: 'Заядлый читатель с 20-летним стажем. Люблю классическую литературу, современную прозу и научно-популярные книги.',
  stats: {
    totalBooks: 45,
    activeBooks: 12,
    completedExchanges: 89,
    followers: 234,
    following: 89
  },

  books: [
    { 
      id: 101, 
      title: 'Анна Каренина', 
      author: 'Лев Толстой', 
      rating: 4.8, 
      genre: 'Классика', 
      distance: '0.8 км', 
      owner: { name: 'Анна К.' }, 
      condition: 'хорошее', 
      status: 'available', 
      image: 'https://placehold.co/400x600/3A342B/E8E2D4?text=Анна+Каренина', 
      location: 'Центральная библиотека', 
      added: '2 дня назад' 
    },
    { 
      id: 102, 
      title: 'Преступление и наказание', 
      author: 'Фёдор Достоевский', 
      rating: 4.7, 
      genre: 'Классика', 
      distance: '1.2 км', 
      owner: { name: 'Анна К.' }, 
      condition: 'новая', 
      status: 'reserved', 
      image: 'https://placehold.co/400x600/3A342B/E8E2D4?text=Преступление', 
      location: 'Парк Сокольники', 
      added: '5 дней назад' 
    },
  ]
};

const UserProfilePage = () => {
  const [activeTab, setActiveTab] = useState('books');
  const user = MOCK_USER_DATA;
  const navigate = useNavigate();

  return (
    <div className="p-4 md:p-8">
       <button onClick={() => navigate(-1)} className="flex items-center mb-6 font-semibold" style={{ color: 'var(--md-sys-color-primary)' }}>
        <ArrowLeft size={20} className="mr-2" />
        Назад
      </button>
      <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
        <div className="lg:col-span-1 space-y-6">
          <div className="p-6 rounded-xl text-center" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
            <div className="relative inline-block mb-4">
              <img src={user.avatar} alt={user.name} className="h-24 w-24 rounded-full mx-auto" />
              {user.verified && (
                <div className="absolute -bottom-1 -right-1 rounded-full p-1" style={{ backgroundColor: 'var(--md-sys-color-primary)' }}>
                  <ShieldCheck className="h-4 w-4" style={{ color: 'var(--md-sys-color-on-primary)' }} />
                </div>
              )}
            </div>
            <h2 className="text-xl font-semibold">{user.name}</h2>
            <div className="flex items-center justify-center gap-1 my-2">
              <Star className="h-4 w-4 text-yellow-400" />
              <span>{user.rating} ({user.reviewsCount})</span>
            </div>
            <div className="text-sm space-y-2" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
              <p className="flex items-center justify-center gap-1"><Calendar className="h-4 w-4" /> С {new Date(user.memberSince).toLocaleDateString('ru-RU')}</p>
              <p className="flex items-center justify-center gap-1"><MapPin className="h-4 w-4" /> {user.location}</p>
            </div>
            <p className="text-sm my-4">{user.bio}</p>
            <button className="w-full py-2 rounded-lg font-semibold" style={{ backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)' }}>
              <Edit size={16} className="inline mr-2" /> Редактировать
            </button>
          </div>
          <div className="p-6 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
             <h3 className="font-semibold text-lg mb-4 flex items-center gap-2"><Trophy size={20}/> Статистика</h3>
             <div className="grid grid-cols-2 gap-4 text-center">
                 <div>
                    <p className="text-2xl font-semibold" style={{color: 'var(--md-sys-color-primary)'}}>{user.stats.totalBooks}</p>
                    <p className="text-xs">Всего книг</p>
                 </div>
                 <div>
                    <p className="text-2xl font-semibold" style={{color: 'var(--md-sys-color-primary)'}}>{user.stats.completedExchanges}</p>
                    <p className="text-xs">Обменов</p>
                 </div>
             </div>
          </div>
        </div>

        <div className="lg:col-span-3">
          <div className="flex border-b mb-6" style={{ borderColor: 'var(--md-sys-color-outline-variant)' }}>
            <button onClick={() => setActiveTab('books')} className={`py-2 px-4 font-semibold ${activeTab === 'books' ? 'border-b-2' : ''}`} style={{ borderColor: activeTab === 'books' ? 'var(--md-sys-color-primary)' : 'transparent', color: activeTab === 'books' ? 'var(--md-sys-color-primary)' : 'var(--md-sys-color-on-surface-variant)' }}>Книги ({user.books.length})</button>
            <button onClick={() => setActiveTab('history')} className={`py-2 px-4 font-semibold ${activeTab === 'history' ? 'border-b-2' : ''}`} style={{ borderColor: activeTab === 'history' ? 'var(--md-sys-color-primary)' : 'transparent', color: activeTab === 'history' ? 'var(--md-sys-color-primary)' : 'var(--md-sys-color-on-surface-variant)' }}>История</button>
            <button onClick={() => setActiveTab('reviews')} className={`py-2 px-4 font-semibold ${activeTab === 'reviews' ? 'border-b-2' : ''}`} style={{ borderColor: activeTab === 'reviews' ? 'var(--md-sys-color-primary)' : 'transparent', color: activeTab === 'reviews' ? 'var(--md-sys-color-primary)' : 'var(--md-sys-color-on-surface-variant)' }}>Отзывы</button>
          </div>

          {activeTab === 'books' && (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {user.books.map(book => (
                <BookCard key={book.id} book={book} onSelect={() => { /* onBookSelect(book.id) */ }} onReserve={() => {}} />
              ))}
            </div>
          )}
          {activeTab === 'history' && <div className="p-6 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>История обменов...</div>}
          {activeTab === 'reviews' && <div className="p-6 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>Отзывы о пользователе...</div>}
        </div>
      </div>
    </div>
  );
};

export default UserProfilePage;