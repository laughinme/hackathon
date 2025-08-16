import React, { useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Calendar, MapPin, Edit, Book as BookIcon } from 'lucide-react';
import BookCard from '../common/BookCard';
import { AuthContext } from '../../App';
import { getMyBooks } from '../../api/services';

export default function UserProfilePage() {
  const navigate = useNavigate();
  const { currentUser } = useContext(AuthContext);
  const [userBooks, setUserBooks] = useState([]);
  const [loadingBooks, setLoadingBooks] = useState(true);

  useEffect(() => {
    const fetchUserBooks = async () => {
      setLoadingBooks(true);
      try {
        const response = await getMyBooks();
        setUserBooks(response.data);
      } catch (error) {
        console.error("Failed to fetch user books", error);
      } finally {
        setLoadingBooks(false);
      }
    };
    if (currentUser) {
        fetchUserBooks();
    }
  }, [currentUser]);

  if (!currentUser) {
    return <div className="p-8 text-center">Загрузка профиля...</div>;
  }
  
  const memberSince = new Date(currentUser.created_at).toLocaleDateString('ru-RU', { year: 'numeric', month: 'long' });

  const renderBooks = () => {
      if (loadingBooks) return <div className="text-center">Загрузка книг...</div>
      if (userBooks.length === 0) return (
        <div className="text-center py-16 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
            <BookIcon className="h-12 w-12 mx-auto mb-4" style={{ color: 'var(--md-sys-color-on-surface-variant)' }} />
            <h3 className="text-lg font-semibold">Вы еще не добавили ни одной книги</h3>
            <button onClick={() => navigate('/add-book')} className="mt-4 py-2 px-4 rounded-lg font-semibold" style={{backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)'}}>
                Добавить первую книгу
            </button>
        </div>
      );
      return (
          <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
            {userBooks.map(book => (
                <BookCard 
                    key={book.id} 
                    book={book}
                    showStatus={true}
                    onSelect={() => navigate(`/book/${book.id}`)} 
                    onReserve={() => {}} 
                />
            ))}
          </div>
      )
  }

  return (
    <div className="p-4 md:p-8">
      <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
        <div className="lg:col-span-1 space-y-6">
          <div className="p-6 rounded-xl text-center" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
            <img src={currentUser.avatar_url || `https://placehold.co/120x120/DBC66E/3A3000?text=${currentUser.username[0]}`} alt={currentUser.username} className="h-24 w-24 rounded-full mx-auto object-cover mb-4" />
            <h2 className="text-xl font-semibold">{currentUser.username}</h2>
            <div className="text-sm space-y-2 mt-2" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
              <p className="flex items-center justify-center gap-1"><Calendar className="h-4 w-4" /> В клубе с {memberSince}</p>
              {currentUser.city && <p className="flex items-center justify-center gap-1"><MapPin className="h-4 w-4" /> {currentUser.city.name}</p>}
            </div>
            <p className="text-sm my-4 text-left">{currentUser.bio || 'Информация о себе не заполнена.'}</p>
            <button onClick={() => navigate('/profile/edit')} className="w-full py-2 rounded-lg font-semibold flex items-center justify-center gap-2" style={{ backgroundColor: 'var(--md-sys-color-primary-container)', color: 'var(--md-sys-color-on-primary-container)' }}>
              <Edit size={16} /> Редактировать
            </button>
          </div>
           <div className="p-6 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
                <h3 className="font-semibold text-lg mb-4">Любимые жанры</h3>
                <div className="flex flex-wrap gap-2">
                    {currentUser.favorite_genres.length > 0 ? currentUser.favorite_genres.map(genre => (
                        <span key={genre.id} className="text-xs px-2 py-1 rounded" style={{backgroundColor: 'var(--md-sys-color-surface-container-high)'}}>{genre.name}</span>
                    )) : (
                        <p className="text-sm text-[var(--md-sys-color-on-surface-variant)]">Жанры не выбраны.</p>
                    )}
                </div>
           </div>
        </div>

        <div className="lg:col-span-3">
            <h2 className="text-2xl font-bold mb-6">Мои книги ({userBooks.length})</h2>
            {renderBooks()}
        </div>
      </div>
    </div>
  );
}