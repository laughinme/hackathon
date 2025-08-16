import React, { useState, useEffect, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, MapPin, Edit, Heart, Share2, Info, Loader2, User } from 'lucide-react';
import { getBookById, likeBook, recordBookClick } from '../../api/services';
import ReserveBookModal from '../common/ReserveBookModal';
import { AuthContext } from '../../App';

export default function BookDetailPage() {
  const { bookId } = useParams();
  const navigate = useNavigate();
  const { currentUser } = useContext(AuthContext);

  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showReserveModal, setShowReserveModal] = useState(false);
  const [activeImage, setActiveImage] = useState(0);
  const [isLiked, setIsLiked] = useState(false);
  const [isProcessingLike, setIsProcessingLike] = useState(false);

  const fetchBook = React.useCallback(async () => {
    setLoading(true);
    try {
      const response = await getBookById(bookId);
      setBook(response.data);
      setIsLiked(response.data.is_liked_by_user);
      setActiveImage(0);
    } catch (err) {
      console.error("Failed to fetch book details", err);
      setError("Не удалось загрузить информацию о книге.");
    } finally {
      setLoading(false);
    }
  }, [bookId]);

  useEffect(() => {
    fetchBook();
    recordBookClick(bookId).catch(err => console.error("Failed to record click", err));
  }, [fetchBook, bookId]);

  const handleReservationSuccess = () => {
     alert("Книга успешно забронирована!");
     fetchBook();
  }

  const handleLikeClick = async () => {
    if (isProcessingLike) return;
    setIsProcessingLike(true);
    try {
      await likeBook(book.id);
      setIsLiked(!isLiked);
    } catch (err) {
      console.error("Failed to toggle like", err);
      alert("Не удалось обработать лайк.");
    } finally {
      setIsProcessingLike(false);
    }
  }

  if (loading) return <div className="p-8 text-center flex items-center justify-center gap-2"><Loader2 className="animate-spin" /> Загрузка...</div>;
  if (error) return <div className="p-8 text-center text-red-400">{error}</div>;
  if (!book) return <div className="p-8 text-center">Книга не найдена.</div>;

  const isOwner = currentUser?.id === book.owner_id;
  const currentImage = book.photo_urls?.[activeImage] || 'https://placehold.co/400x600/3A342B/E8E2D4?text=No+Image';

  return (
    <>
    {showReserveModal && (
        <ReserveBookModal book={book} onClose={() => setShowReserveModal(false)} onSuccess={handleReservationSuccess} />
    )}
    <div className="p-4 md:p-8">
      <button onClick={() => navigate(-1)} className="flex items-center mb-6 font-semibold" style={{ color: 'var(--md-sys-color-primary)' }}>
        <ArrowLeft size={20} className="mr-2" />
        Назад
      </button>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2 space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 p-6 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
            <div>
                 <img src={currentImage} alt={book.title} className="w-full aspect-[3/4] rounded-lg object-cover mb-2" />
                 {book.photo_urls && book.photo_urls.length > 1 && (
                     <div className="flex gap-2">
                        {book.photo_urls.map((url, index) => (
                            <img 
                                key={index} 
                                src={url} 
                                alt={`thumbnail ${index}`}
                                onClick={() => setActiveImage(index)}
                                className={`w-16 h-16 rounded object-cover cursor-pointer border-2 ${activeImage === index ? 'border-[var(--md-sys-color-primary)]' : 'border-transparent'}`}
                            />
                        ))}
                     </div>
                 )}
            </div>
            
            <div className="space-y-4 flex flex-col">
              <h1 className="text-3xl font-bold">{book.title}</h1>
              <p className="text-xl" style={{color: 'var(--md-sys-color-on-surface-variant)'}}>{book.author?.name || 'Автор неизвестен'}</p>
              <div className="flex flex-wrap gap-2 items-center">
                 <span className="text-sm font-medium px-2 py-1 rounded" style={{ backgroundColor: 'var(--md-sys-color-tertiary-container)', color: 'var(--md-sys-color-on-tertiary-container)' }}>{book.genre?.name || 'Жанр не указан'}</span>
                 <span className="text-sm font-medium px-2 py-1 rounded capitalize" style={{ backgroundColor: 'var(--md-sys-color-secondary-container)', color: 'var(--md-sys-color-on-secondary-container)' }}>{book.condition}</span>
              </div>
              <div className="pt-4 border-t" style={{borderColor: 'var(--md-sys-color-outline-variant)'}}>
                <p><strong className="font-semibold">Язык:</strong> {book.language_code?.toUpperCase()}</p>
                {book.pages && <p><strong className="font-semibold">Страниц:</strong> {book.pages}</p>}
              </div>
              <div className="flex-grow"></div>
              <div className="flex items-center gap-2">
                 <button 
                    onClick={handleLikeClick} 
                    disabled={isProcessingLike}
                    className="p-3 rounded-lg flex-1 font-semibold flex items-center justify-center gap-2 transition-colors"
                    style={{
                        backgroundColor: isLiked ? 'var(--md-sys-color-primary)' : 'var(--md-sys-color-primary-container)', 
                        color: isLiked ? 'var(--md-sys-color-on-primary)' : 'var(--md-sys-color-on-primary-container)'
                    }}
                  >
                    <Heart size={20} fill={isLiked ? 'currentColor' : 'none'} /> {isLiked ? 'В лайках' : 'Лайк'}
                 </button>
                 <button className="p-3 rounded-lg font-semibold" style={{backgroundColor: 'var(--md-sys-color-secondary-container)', color: 'var(--md-sys-color-on-secondary-container)'}}>
                    <Share2 size={20} />
                 </button>
              </div>
            </div>
          </div>
          <div className="p-6 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
            <h3 className="text-xl font-bold mb-2">Описание</h3>
            <p style={{color: 'var(--md-sys-color-on-surface-variant)'}}>{book.description || "Владелец не добавил описание."}</p>
          </div>
           {book.extra_terms && (
             <div className="p-6 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
                <h3 className="text-xl font-bold mb-2 flex items-center gap-2"><Info size={20}/> Дополнительные условия</h3>
                <p style={{color: 'var(--md-sys-color-on-surface-variant)'}}>{book.extra_terms}</p>
            </div>
           )}
        </div>

        <div className="space-y-6">
          <div className="p-6 rounded-xl space-y-4" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
             <h3 className="text-xl font-bold">{isOwner ? 'Это ваша книга' : book.is_available ? 'Доступна для обмена' : 'Уже забронирована'}</h3>
            <div className="flex items-start gap-2 text-sm"><MapPin size={16} className="mt-1 flex-shrink-0" /> {book.exchange_location?.address || 'Адрес не указан'}</div>
            <button 
                onClick={() => isOwner ? navigate(`/book/${book.id}/edit`) : setShowReserveModal(true)}
                disabled={!book.is_available && !isOwner} 
                className="w-full py-3 rounded-lg font-semibold disabled:cursor-not-allowed flex items-center justify-center gap-2" 
                style={{ backgroundColor: (!book.is_available && !isOwner) ? 'var(--md-sys-color-surface-variant)' : 'var(--md-sys-color-primary)', color: (!book.is_available && !isOwner) ? 'var(--md-sys-color-on-surface-variant)' : 'var(--md-sys-color-on-primary)' }}>
              {isOwner ? <><Edit size={16} />Редактировать</> : (book.is_available ? 'Забронировать книгу' : 'Недоступна')}
            </button>
          </div>
          <div className="p-6 rounded-xl space-y-4" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
            <h3 className="text-xl font-bold">Владелец</h3>
            <div className="flex items-center gap-3">
              <img src={book.owner?.avatar_url || 'https://placehold.co/80x80/DBC66E/3A3000?text=?'} alt={book.owner?.username} className="w-12 h-12 rounded-full object-cover" />
              <div>
                <p className="font-semibold">{book.owner?.username || 'Имя не указано'}</p>
                <p className="text-sm flex items-center gap-1"><User size={14} /> Возраст: {book.owner?.age || 'не указан'}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    </>
  );
}