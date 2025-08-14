import React, { useState, useEffect, useCallback } from 'react';
import { getOwnedExchanges, getRequestedExchanges, acceptExchange, declineExchange, cancelExchange } from '../../api/services';
import { ArrowLeft, Clock, User, Check, X, ThumbsDown, Loader2 } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const ExchangeCard = ({ exchange, type, onAction }) => {
    const { book, owner, requester, progress, id } = exchange;
    const isOwner = type === 'owned';
    const partner = isOwner ? requester : owner;

    const handleAction = (action) => {
        if (action === 'decline' || action === 'cancel') {
            const reason = prompt("Укажите причину (необязательно):");
            onAction(action, id, reason || "Действие выполнено без указания причины");
        } else {
            onAction(action, id);
        }
    };

    const renderActions = () => {
        if (progress === 'created' && isOwner) {
            return (
                <div className="flex gap-2 mt-auto pt-2">
                    <button onClick={() => handleAction('accept')} className="flex-1 text-sm bg-green-600 hover:bg-green-500 text-white font-semibold py-2 px-3 rounded-lg flex items-center justify-center gap-1"><Check size={16}/> Принять</button>
                    <button onClick={() => handleAction('decline')} className="flex-1 text-sm bg-red-600 hover:bg-red-500 text-white font-semibold py-2 px-3 rounded-lg flex items-center justify-center gap-1"><X size={16}/> Отклонить</button>
                </div>
            );
        }
        if (progress === 'created' && !isOwner) {
             return <div className="mt-auto pt-2"><button onClick={() => handleAction('cancel')} className="bg-gray-500 hover:bg-gray-400 text-white font-semibold py-2 px-3 rounded-lg w-full text-sm">Отменить запрос</button></div>;
        }
        if (progress === 'accepted') {
             return <div className="mt-auto pt-2"><button onClick={() => handleAction('cancel')} className="bg-red-600 hover:bg-red-500 text-white font-semibold py-2 px-3 rounded-lg w-full text-sm">Отменить обмен</button></div>;
        }
        return null;
    };

    const statusMap = {
        created: { text: "Ожидает", color: "bg-yellow-500/20 text-yellow-300" },
        accepted: { text: "Принят", color: "bg-blue-500/20 text-blue-300" },
        declined: { text: "Отклонен", color: "bg-red-500/20 text-red-300" },
        finished: { text: "Завершен", color: "bg-green-500/20 text-green-300" },
        canceled: { text: "Отменен", color: "bg-gray-500/20 text-gray-300" },
    };

    return (
        <div className="p-4 rounded-xl flex gap-4" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
            <img src={book.photo_urls[0] || `https://placehold.co/200x300/3A342B/E8E2D4?text=No+Image`} alt={book.title} className="w-24 h-36 rounded-lg object-cover flex-shrink-0" />
            <div className="flex-1 flex flex-col">
                <div className="flex justify-between items-start">
                    <h3 className="font-bold">{book.title}</h3>
                    <span className={`text-xs font-medium px-2 py-1 rounded-full whitespace-nowrap ${statusMap[progress]?.color}`}>{statusMap[progress]?.text}</span>
                </div>
                <p className="text-sm" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>{book.author.name}</p>
                <div className="mt-2 text-sm space-y-1" style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
                    <p className="flex items-center gap-2"><User size={14} />{isOwner ? 'От' : 'С'}: {partner.username}</p>
                    <p className="flex items-center gap-2"><Clock size={14} /> {new Date(exchange.created_at).toLocaleDateString()}</p>
                </div>
                {renderActions()}
            </div>
        </div>
    )
}

export default function ExchangesPage() {
    const [activeTab, setActiveTab] = useState('owned');
    const [exchanges, setExchanges] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const fetchData = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            const fetchFunction = activeTab === 'owned' ? getOwnedExchanges : getRequestedExchanges;
            const response = await fetchFunction({ only_active: false });
            setExchanges(response.data);
        } catch (err) {
            console.error("Failed to fetch exchanges:", err);
            setError("Не удалось загрузить обмены.");
        } finally {
            setLoading(false);
        }
    }, [activeTab]);

    useEffect(() => {
        fetchData();
    }, [fetchData]);

    const handleAction = async (action, exchangeId, reason) => {
        try {
            switch(action) {
                case 'accept': await acceptExchange(exchangeId); break;
                case 'decline': await declineExchange(exchangeId, { cancel_reason: reason }); break;
                case 'cancel': await cancelExchange(exchangeId, { cancel_reason: reason }); break;
                default: return;
            }
            fetchData();
        } catch (err) {
            console.error(`Failed to ${action} exchange:`, err);
            alert(`Ошибка: не удалось выполнить действие.`);
        }
    };

    const renderContent = () => {
        if (loading) return <div className="text-center p-8 flex items-center justify-center gap-2"><Loader2 className="animate-spin" />Загрузка...</div>;
        if (error) return <div className="text-center p-8 text-red-400">{error}</div>;
        if (exchanges.length === 0) {
            return (
                <div className="text-center py-16 rounded-xl" style={{ backgroundColor: 'var(--md-sys-color-surface-container)' }}>
                    <ThumbsDown className="h-12 w-12 mx-auto mb-4" style={{ color: 'var(--md-sys-color-on-surface-variant)' }} />
                    <h3 className="text-lg font-semibold">Пусто</h3>
                    <p style={{ color: 'var(--md-sys-color-on-surface-variant)' }}>
                        {activeTab === 'owned' ? "Вам пока не предлагали обменов." : "Вы еще не запрашивали обмены."}
                    </p>
                </div>
            );
        }
        return <div className="space-y-4">{exchanges.map(ex => <ExchangeCard key={ex.id} exchange={ex} type={activeTab} onAction={handleAction}/>)}</div>
    };

    return (
        <div className="p-4 md:p-8 max-w-4xl mx-auto">
            <div className="flex items-center gap-4 mb-6">
                <button onClick={() => navigate(-1)} className="flex items-center gap-2 font-semibold" style={{ color: 'var(--md-sys-color-primary)' }}>
                    <ArrowLeft className="h-5 w-5" /> Назад
                </button>
                <h1 className="text-2xl font-bold">Мои обмены</h1>
            </div>
            <div className="flex border-b mb-6" style={{ borderColor: 'var(--md-sys-color-outline-variant)' }}>
                <button onClick={() => setActiveTab('owned')} className={`py-2 px-4 font-semibold ${activeTab === 'owned' ? 'border-b-2' : ''}`} style={{ borderColor: activeTab === 'owned' ? 'var(--md-sys-color-primary)' : 'transparent', color: activeTab === 'owned' ? 'var(--md-sys-color-primary)' : 'var(--md-sys-color-on-surface-variant)' }}>Мне предлагают</button>
                <button onClick={() => setActiveTab('requested')} className={`py-2 px-4 font-semibold ${activeTab === 'requested' ? 'border-b-2' : ''}`} style={{ borderColor: activeTab === 'requested' ? 'var(--md-sys-color-primary)' : 'transparent', color: activeTab === 'requested' ? 'var(--md-sys-color-primary)' : 'var(--md-sys-color-on-surface-variant)' }}>Мои запросы</button>
            </div>
            {renderContent()}
        </div>
    );
}