import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Heart, Loader2, ArrowLeft } from 'lucide-react';
import { getAllBooks } from '../../api/services';
import BookCard from '../common/BookCard';

export default function LikedBooksPage() {
    const [likedBooks, setLikedBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchLikedBooks = async () => {
            setLoading(true);
            try {
                const response = await getAllBooks({ limit: 200 });
                const filteredBooks = response.data.filter(book => book.is_liked_by_user);
                setLikedBooks(filteredBooks);
            } catch (err) {
                console.error("Failed to fetch liked books", err);
                setError("Не удалось загрузить понравившиеся книги.");
            } finally {
                setLoading(false);
            }
        };
        fetchLikedBooks();
    }, []);
    
    const handleLikeToggle = (bookId, newLikeStatus) => {
      if (!newLikeStatus) {
        setLikedBooks(currentBooks => currentBooks.filter(b => b.id !== bookId));
      }
    };


    const renderContent = () => {
        if (loading) return <div className="text-center p-8 flex items-center justify-center gap-2"><Loader2 className="animate-spin" /> Загрузка...</div>;
        if (error) return <div className="text-center p-8 text-red-400">{error}</div>;
        if (likedBooks.length === 0) {
            return (
                <div className="text-center py-16 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
                    <Heart className="h-12 w-12 mx-auto mb-4" style={{ color: 'var(--md-sys-color-on-surface-variant)' }} />
                    <h3 className="text-lg font-semibold">У вас пока нет понравившихся книг</h3>
                    <p style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>Нажимайте на сердечко у книг, чтобы добавить их сюда.</p>
                </div>
            );
        }
        return (
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
                {likedBooks.map(book => (
                    <BookCard
                        key={book.id}
                        book={book}
                        onSelect={() => navigate(`/book/${book.id}`)}
                        onLikeToggle={handleLikeToggle}
                    />
                ))}
            </div>
        );
    };

    return (
        <div className="p-4 md:p-8">
            <div className="flex items-center gap-4 mb-6">
                 <button onClick={() => navigate(-1)} className="flex items-center gap-2 font-semibold" style={{ color: 'var(--md-sys-color-primary)' }}>
                    <ArrowLeft className="h-5 w-5" /> Назад
                </button>
                <div className="h-6 w-px" style={{backgroundColor: 'var(--md-sys-color-outline-variant)'}}></div>
                <div className="flex items-center gap-2">
                    <Heart className="h-6 w-6" style={{ color: 'var(--md-sys-color-primary)' }} />
                    <h1 className="text-2xl font-bold">Мои лайки</h1>
                </div>
            </div>
            {renderContent()}
        </div>
    );
}