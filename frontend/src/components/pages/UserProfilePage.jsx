import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ArrowLeft, Star, Calendar, MapPin, Edit, Shield, Trophy, Clock, Users, Book as BookIcon, Plus } from 'lucide-react';
import BookCard from '../common/BookCard';

export default function UserProfilePage({ allBooks }) {
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('books');

  const user = {
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
      following: 89,
      savedBooks: 156
    },
    achievements: [
        { id: '1', name: 'Книголюб', description: '50+ книг в коллекции', icon: '📚' },
        { id: '2', name: 'Надёжный партнёр', description: '100+ успешных обменов', icon: '🤝' },
        { id: '3', name: 'Ценитель классики', description: 'Много классической литературы', icon: '🎭' }
    ]
  };

  
  const userBooks = allBooks.filter(book => book.owner.name === 'Анна К.');

  const exchangeHistory = [
    { id: '1', bookTitle: 'Война и мир', partnerName: 'Михаил П.', date: '2024-01-20', status: 'completed', rating: 5 },
    { id: '2', bookTitle: 'Мастер и Маргарита', partnerName: 'Елена С.', date: '2024-01-15', status: 'completed', rating: 4 },
    { id: '3', bookTitle: 'Гарри Поттер', partnerName: 'Дмитрий В.', date: '2024-01-10', status: 'in-progress', rating: null }
  ];

  const userReviews = [
    { id: '1', reviewer: 'Михаил П.', rating: 5, date: '2024-01-20', comment: 'Анна - отличный партнёр для обмена! Книги в прекрасном состоянии.' },
    { id: '2', reviewer: 'Елена С.', rating: 5, date: '2024-01-15', comment: 'Быстрый и удобный обмен. Рекомендую!' }
  ];

  const getStatusLabel = (status) => {
    switch (status) {
      case 'completed': return 'Завершён';
      case 'in-progress': return 'В процессе';
      default: return 'Неизвестно';
    }
  };

  const getStatusStyle = (status) => {
    switch (status) {
      case 'completed': return { backgroundColor: 'var(--md-sys-color-tertiary-container)', color: 'var(--md-sys-color-on-tertiary-container)' };
      case 'in-progress': return { backgroundColor: 'var(--md-sys-color-primary-container)', color: 'var(--md-sys-color-on-primary-container)' };
      default: return { backgroundColor: 'var(--md-sys-color-surface-variant)', color: 'var(--md-sys-color-on-surface-variant)' };
    }
  };

  const renderStars = (rating) => (
    <div className="flex items-center gap-1">
      {Array.from({ length: 5 }).map((_, i) => (
        <Star key={i} className={`h-4 w-4 ${i < rating ? 'text-yellow-400 fill-yellow-400' : 'text-gray-400'}`} />
      ))}
    </div>
  );

  return (
    <div className="p-4 md:p-8">
      <div className="flex items-center justify-between mb-6">
        <button onClick={() => navigate(-1)} className="flex items-center font-semibold gap-2" style={{ color: 'var(--md-sys-color-primary)' }}>
          <ArrowLeft className="h-5 w-5" />
          Назад
        </button>
        <div className="flex items-center gap-2">
            <button 
              onClick={() => navigate('/add-book')} 
              className="flex items-center gap-2 py-2 px-3 rounded-lg text-sm font-semibold" 
              style={{ backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)' }}>
                <Plus className="h-4 w-4" /> Добавить книгу
            </button>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
        <div className="lg:col-span-1 space-y-6">
          <div className="p-6 rounded-xl text-center" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
            <div className="relative inline-block mb-4">
              <img src={user.avatar} alt={user.name} className="h-24 w-24 rounded-full mx-auto" />
              {user.verified && (
                <div className="absolute -bottom-1 -right-1 rounded-full p-1" style={{ backgroundColor: 'var(--md-sys-color-primary)' }}>
                  <Shield className="h-4 w-4" style={{ color: 'var(--md-sys-color-on-primary)' }} />
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
            <p className="text-sm my-4 text-left">{user.bio}</p>
            <button className="w-full py-2 rounded-lg font-semibold flex items-center justify-center gap-2" style={{ backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)' }}>
              <Edit size={16} /> Редактировать
            </button>
          </div>

          <div className="p-6 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
            <h3 className="font-semibold text-lg mb-4 flex items-center gap-2"><Trophy size={20}/> Статистика</h3>
            <div className="grid grid-cols-2 gap-4 text-center mb-4">
              <div><p className="text-2xl font-semibold" style={{color: 'var(--md-sys-color-primary)'}}>{userBooks.length}</p><p className="text-xs">Всего книг</p></div>
              <div><p className="text-2xl font-semibold" style={{color: 'var(--md-sys-color-primary)'}}>{user.stats.completedExchanges}</p><p className="text-xs">Обменов</p></div>
              <div><p className="text-2xl font-semibold">{user.stats.followers}</p><p className="text-xs">Подписчиков</p></div>
              <div><p className="text-2xl font-semibold">{user.stats.following}</p><p className="text-xs">Подписок</p></div>
            </div>
            <div className="border-t pt-4" style={{borderColor: 'var(--md-sys-color-outline-variant)'}}>
                <div className="flex justify-between text-sm mb-1"><span>Активные книги</span><span>{userBooks.filter(b => b.status === 'available').length} из {userBooks.length}</span></div>
                <div className="w-full h-2 rounded-full" style={{backgroundColor: 'var(--md-sys-color-surface-variant)'}}>
                    <div className="h-2 rounded-full" style={{width: `${(userBooks.filter(b => b.status === 'available').length / userBooks.length) * 100 || 0}%`, backgroundColor: 'var(--md-sys-color-primary)'}}></div>
                </div>
            </div>
          </div>
          
          <div className="p-6 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
            <h3 className="font-semibold text-lg mb-4 flex items-center gap-2"><Trophy size={20}/> Достижения</h3>
            <div className="space-y-3">
              {user.achievements.map((ach) => (
                <div key={ach.id} className="flex items-center gap-3 p-2 rounded-lg" style={{backgroundColor: 'var(--md-sys-color-surface-container-high)'}}>
                    <span className="text-lg">{ach.icon}</span>
                    <div>
                        <p className="font-medium text-sm">{ach.name}</p>
                        <p className="text-xs" style={{color: 'var(--md-sys-color-on-surface-variant)'}}>{ach.description}</p>
                    </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className="lg:col-span-3">
          <div className="flex border-b mb-6" style={{ borderColor: 'var(--md-sys-color-outline-variant)' }}>
            <button onClick={() => setActiveTab('books')} className={`py-2 px-4 font-semibold ${activeTab === 'books' ? 'border-b-2' : ''}`} style={{ borderColor: activeTab === 'books' ? 'var(--md-sys-color-primary)' : 'transparent', color: activeTab === 'books' ? 'var(--md-sys-color-primary)' : 'var(--md-sys-color-on-surface-variant)' }}>Книги ({userBooks.length})</button>
            <button onClick={() => setActiveTab('history')} className={`py-2 px-4 font-semibold ${activeTab === 'history' ? 'border-b-2' : ''}`} style={{ borderColor: activeTab === 'history' ? 'var(--md-sys-color-primary)' : 'transparent', color: activeTab === 'history' ? 'var(--md-sys-color-primary)' : 'var(--md-sys-color-on-surface-variant)' }}>История ({exchangeHistory.length})</button>
            <button onClick={() => setActiveTab('reviews')} className={`py-2 px-4 font-semibold ${activeTab === 'reviews' ? 'border-b-2' : ''}`} style={{ borderColor: activeTab === 'reviews' ? 'var(--md-sys-color-primary)' : 'transparent', color: activeTab === 'reviews' ? 'var(--md-sys-color-primary)' : 'var(--md-sys-color-on-surface-variant)' }}>Отзывы ({userReviews.length})</button>
            <button onClick={() => setActiveTab('saved')} className={`py-2 px-4 font-semibold ${activeTab === 'saved' ? 'border-b-2' : ''}`} style={{ borderColor: activeTab === 'saved' ? 'var(--md-sys-color-primary)' : 'transparent', color: activeTab === 'saved' ? 'var(--md-sys-color-primary)' : 'var(--md-sys-color-on-surface-variant)' }}>Избранное ({user.stats.savedBooks})</button>
          </div>

          {activeTab === 'books' && (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {userBooks.map(book => (<BookCard key={book.id} book={book} onReserve={() => {}} onSelect={() => {}} />))}
            </div>
          )}

          {activeTab === 'history' && (
            <div className="space-y-4">
              {exchangeHistory.map(ex => (
                <div key={ex.id} className="p-4 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
                  <div className="flex items-center justify-between mb-2">
                    <h4 className="font-semibold">{ex.bookTitle}</h4>
                    <span className="text-xs font-medium px-2 py-1 rounded" style={getStatusStyle(ex.status)}>{getStatusLabel(ex.status)}</span>
                  </div>
                  <div className="flex items-center gap-4 text-sm mb-2" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
                    <span className="flex items-center gap-1"><Users size={14} /> с {ex.partnerName}</span>
                    <span className="flex items-center gap-1"><Clock size={14} /> {new Date(ex.date).toLocaleDateString('ru-RU')}</span>
                  </div>
                  {ex.rating && renderStars(ex.rating)}
                </div>
              ))}
            </div>
          )}

          {activeTab === 'reviews' && (
            <div className="space-y-4">
              {userReviews.map(review => (
                <div key={review.id} className="p-4 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
                  <div className="flex items-start gap-3">
                    <img src={`https://placehold.co/40x40/DBC66E/3A3000?text=${review.reviewer[0]}`} alt={review.reviewer} className="h-10 w-10 rounded-full" />
                    <div>
                      <div className="flex items-center gap-2 mb-1">
                        <span className="font-semibold">{review.reviewer}</span>
                        <span className="text-xs" style={{color: 'var(--md-sys-color-on-surface-variant)'}}>{new Date(review.date).toLocaleDateString('ru-RU')}</span>
                      </div>
                      <div className="mb-2">{renderStars(review.rating)}</div>
                      <p className="text-sm" style={{color: 'var(--md-sys-color-on-surface-variant)'}}>{review.comment}</p>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}

          {activeTab === 'saved' && (
             <div className="text-center py-16 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
                <BookIcon className="h-12 w-12 mx-auto mb-4" style={{ color: 'var(--md-sys-color-on-surface-variant)' }} />
                <h3 className="text-lg font-semibold">Избранные книги</h3>
                <p style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>Здесь будут ваши сохраненные книги</p>
            </div>
          )}

        </div>
      </div>
    </div>
  );
}