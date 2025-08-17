import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { ArrowLeft, Book, Save, Loader2 } from 'lucide-react';
import { getBookById, editBook, getGenres, getAuthors, getExchangeLocations } from '../../api/services';

export default function EditBookPage() {
    const { bookId } = useParams();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);
    const [genres, setGenres] = useState([]);
    const [authors, setAuthors] = useState([]);
    const [exchangeLocations, setExchangeLocations] = useState([]);

    const { register, handleSubmit, formState: { errors, isSubmitting }, reset } = useForm();

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const [bookRes, genresRes, authorsRes, locationsRes] = await Promise.all([
                    getBookById(bookId),
                    getGenres(),
                    getAuthors(),
                    getExchangeLocations(false)
                ]);

                const bookData = bookRes.data;
                reset({
                    title: bookData.title,
                    description: bookData.description,
                    author_id: bookData.author.id,
                    genre_id: bookData.genre.id,
                    condition: bookData.condition,
                    exchange_location_id: bookData.exchange_location.id,
                    is_available: bookData.is_available,
                });
                
                setGenres(genresRes.data);
                setAuthors(authorsRes.data);
                setExchangeLocations(locationsRes.data);

            } catch (error) {
                console.error("Failed to load data for editing", error);
                alert("Не удалось загрузить данные книги.");
                navigate(`/book/${bookId}`);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [bookId, reset, navigate]);

    const handleFormSubmit = async (data) => {
        const payload = {
            ...data,
            author_id: parseInt(data.author_id, 10),
            genre_id: parseInt(data.genre_id, 10),
            exchange_location_id: parseInt(data.exchange_location_id, 10),
        };
        try {
            await editBook(bookId, payload);
            alert("Книга успешно обновлена!");
            navigate(`/book/${bookId}`);
        } catch (err) {
            console.error("Failed to edit book", err);
            alert(err.response?.data?.detail || "Произошла ошибка при редактировании.");
        }
    };
    
    if (loading) {
        return <div className="p-8 text-center flex items-center justify-center gap-2"><Loader2 className="animate-spin" /> Загрузка редактора...</div>;
    }

    return (
        <div className="p-8 max-w-2xl mx-auto">
            <div className="flex items-center gap-4 mb-6">
                 <button onClick={() => navigate(-1)} className="flex items-center gap-2 font-semibold" style={{ color: 'var(--md-sys-color-primary)' }}>
                    <ArrowLeft className="h-5 w-5" /> Назад
                </button>
                 <div className="h-6 w-px" style={{backgroundColor: 'var(--md-sys-color-outline-variant)'}}></div>
                <div className="flex items-center gap-2">
                    <Book className="h-5 w-5" style={{ color: 'var(--md-sys-color-primary)' }}/>
                    <h1 className="text-xl font-bold">Редактировать книгу</h1>
                </div>
            </div>

            <form onSubmit={handleSubmit(handleFormSubmit)} className="space-y-6 p-6 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
                 <div>
                    <label htmlFor="title" className="block text-sm font-medium mb-1">Название книги *</label>
                    <input id="title" {...register('title', { required: 'Название обязательно' })} className="w-full p-2 rounded-lg border bg-transparent" />
                    {errors.title && <p className="text-sm text-red-400 mt-1">{errors.title.message}</p>}
                </div>

                <div>
                    <label htmlFor="description" className="block text-sm font-medium mb-1">Описание</label>
                    <textarea id="description" {...register('description')} rows="4" className="w-full p-2 rounded-lg border bg-transparent" />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                     <div>
                        <label htmlFor="author_id" className="block text-sm font-medium mb-1">Автор *</label>
                        <select id="author_id" {...register('author_id', { required: true })} className="w-full p-2 rounded-lg border bg-transparent">
                            {authors.map(a => <option key={a.id} value={a.id}>{a.name}</option>)}
                        </select>
                    </div>
                     <div>
                        <label htmlFor="genre_id" className="block text-sm font-medium mb-1">Жанр *</label>
                        <select id="genre_id" {...register('genre_id', { required: true })} className="w-full p-2 rounded-lg border bg-transparent">
                            {genres.map(g => <option key={g.id} value={g.id}>{g.name}</option>)}
                        </select>
                    </div>
                    <div>
                        <label className="block text-sm font-medium mb-1">Состояние *</label>
                        <select {...register('condition')} className="w-full p-2 rounded-lg border bg-transparent capitalize">
                            {['new', 'perfect', 'good', 'normal'].map(c => <option key={c} value={c}>{c}</option>)}
                        </select>
                    </div>
                     <div>
                        <label htmlFor="exchange_location_id" className="block text-sm font-medium mb-1">Точка обмена *</label>
                        <select id="exchange_location_id" {...register('exchange_location_id', { required: true })} className="w-full p-2 rounded-lg border bg-transparent">
                            {exchangeLocations.map(loc => <option key={loc.id} value={loc.id}>{loc.title}</option>)}
                        </select>
                    </div>
                </div>
                 <div className="flex items-center gap-2">
                    <input type="checkbox" id="is_available" {...register('is_available')} className="w-4 h-4" />
                    <label htmlFor="is_available">Книга доступна для обмена</label>
                </div>
                <div className="flex justify-end gap-4 pt-4">
                    <button type="button" onClick={() => navigate(-1)} className="py-2 px-4 rounded-lg font-semibold">Отмена</button>
                    <button type="submit" disabled={isSubmitting} className="py-2 px-4 rounded-lg font-semibold flex items-center gap-2" style={{backgroundColor: 'var(--md-sys-color-primary)', color: 'var(--md-sys-color-on-primary)'}}>
                        <Save size={16} /> {isSubmitting ? "Сохранение..." : "Сохранить"}
                    </button>
                </div>
            </form>
        </div>
    );
}