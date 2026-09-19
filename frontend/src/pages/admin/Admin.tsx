import { useState, useEffect, useCallback, useMemo } from 'react';
import { Link, Outlet, useLocation } from 'react-router-dom';
import {
  Film, MapPin, Clapperboard, CalendarClock, LayoutDashboard,
  ArrowLeft, Plus, Pencil, Trash2, Search, RefreshCw,
} from 'lucide-react';
import { movieApi, genreApi, languageApi, personApi, movieGenreApi, movieLanguageApi, moviePersonApi } from '../../api/movies';
import { theatreApi, screenApi } from '../../api/theatres';
import { showApi } from '../../api/shows';
import { cityApi } from '../../api/cities';
import { bookingApi } from '../../api/bookings';
import type {
  MovieResponse, MovieCreateRequest, MovieUpdateRequest,
  TheatreResponse, TheatreCreateRequest, TheatreUpdateRequest,
  ShowResponse, ShowCreateRequest, ShowUpdateRequest,
  CityResponse, ScreenResponse, GenreResponse, LanguageResponse, PersonResponse,
  MovieGenreResponse, MovieLanguageResponse, MoviePersonResponse,
  BookingResponse,
} from '../../types';
import { MovieStatus } from '../../types';
import { Modal } from '../../components/ui/Modal';
import { Input, Select } from '../../components/ui/Input';
import { Button } from '../../components/ui/Button';
import { useToast } from '../../context/ToastContext';
import { ApiError } from '../../api/client';
import './Admin.css';

/* ============================================================
   Admin Sidebar
   ============================================================ */
function AdminSidebar() {
  const location = useLocation();
  const links = [
    { to: '/admin', icon: LayoutDashboard, label: 'Dashboard', exact: true },
    { to: '/admin/movies', icon: Film, label: 'Movies' },
    { to: '/admin/theatres', icon: MapPin, label: 'Theatres' },
    { to: '/admin/shows', icon: CalendarClock, label: 'Shows' },
  ];

  return (
    <aside className="cv-admin-sidebar">
      <div className="cv-admin-sidebar-header">
        <Clapperboard size={20} />
        <span>Admin</span>
      </div>
      <nav className="cv-admin-sidebar-nav">
        {links.map(link => {
          const isActive = link.exact
            ? location.pathname === link.to
            : location.pathname.startsWith(link.to);
          return (
            <Link
              key={link.to}
              to={link.to}
              className={`cv-admin-nav-item ${isActive ? 'cv-admin-nav-item--active' : ''}`}
            >
              <link.icon size={16} />
              <span>{link.label}</span>
            </Link>
          );
        })}
      </nav>
      <div className="cv-admin-sidebar-footer">
        <Link to="/" className="cv-admin-nav-item">
          <ArrowLeft size={16} />
          <span>Back to App</span>
        </Link>
      </div>
    </aside>
  );
}

/* ============================================================
   Admin Layout
   ============================================================ */
export function AdminLayout() {
  return (
    <div className="cv-admin-layout">
      <AdminSidebar />
      <main className="cv-admin-main">
        <Outlet />
      </main>
    </div>
  );
}

/* ============================================================
   Admin Dashboard
   ============================================================ */
export function AdminDashboard() {
  const [counts, setCounts] = useState({ movies: 0, theatres: 0, shows: 0, cities: 0 });
  const [recentBookings, setRecentBookings] = useState<BookingResponse[]>([]);
  const [recentMovies, setRecentMovies] = useState<MovieResponse[]>([]);

  useEffect(() => {
    const load = async () => {
      try {
        const [m, t, s, c] = await Promise.all([
          movieApi.getAll(),
          theatreApi.getAll(),
          showApi.getAll(),
          cityApi.getAll(),
        ]);
        setCounts({ movies: m.length, theatres: t.length, shows: s.length, cities: c.length });
        setRecentMovies(m.slice(-5).reverse());
      } catch { /* silently fail */ }
    };
    load();
  }, []);

  const kpis = [
    { label: 'Movies', value: counts.movies, icon: Film, color: '#a78bfa' },
    { label: 'Theatres', value: counts.theatres, icon: MapPin, color: '#3b82f6' },
    { label: 'Shows', value: counts.shows, icon: CalendarClock, color: '#22c55e' },
    { label: 'Cities', value: counts.cities, icon: LayoutDashboard, color: '#a855f7' },
  ];

  return (
    <div>
      <h1 className="cv-admin-page-title">Dashboard</h1>
      <div className="cv-admin-kpi-grid">
        {kpis.map(kpi => (
          <div key={kpi.label} className="cv-admin-kpi-card">
            <div className="cv-admin-kpi-icon" style={{ color: kpi.color, background: `${kpi.color}18` }}>
              <kpi.icon size={22} />
            </div>
            <div className="cv-admin-kpi-value">{kpi.value}</div>
            <div className="cv-admin-kpi-label">{kpi.label}</div>
          </div>
        ))}
      </div>

      {recentMovies.length > 0 && (
        <div className="cv-admin-section">
          <h2 className="cv-admin-section-title">Recent Movies</h2>
          <div className="cv-admin-recent-list">
            {recentMovies.map(m => (
              <div key={m.id} className="cv-admin-recent-item">
                <div className="cv-admin-recent-poster">
                  <img
                    src={m.posterUrl}
                    alt={m.title}
                    onError={e => { (e.target as HTMLImageElement).src = `https://placehold.co/60x90/1a1a24/a78bfa?text=${encodeURIComponent(m.title.slice(0, 4))}`; }}
                  />
                </div>
                <div className="cv-admin-recent-info">
                  <div className="cv-admin-recent-name">{m.title}</div>
                  <div className="cv-admin-recent-meta">
                    <span className={`cv-admin-status cv-admin-status--${m.movieStatus?.toLowerCase()}`}>
                      {m.movieStatus}
                    </span>
                    <span>{m.durationInMinutes}m</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      <div className="cv-admin-section">
        <h2 className="cv-admin-section-title">Quick Actions</h2>
        <div className="cv-admin-quick-actions">
          <Link to="/admin/movies" className="cv-admin-quick-action-card">
            <Film size={20} />
            <span>Manage Movies</span>
          </Link>
          <Link to="/admin/theatres" className="cv-admin-quick-action-card">
            <MapPin size={20} />
            <span>Manage Theatres</span>
          </Link>
          <Link to="/admin/shows" className="cv-admin-quick-action-card">
            <CalendarClock size={20} />
            <span>Manage Shows</span>
          </Link>
        </div>
      </div>
    </div>
  );
}

/* ============================================================
   Admin Movies — Full CRUD
   ============================================================ */
export function AdminMovies() {
  const { addToast } = useToast();
  const [movies, setMovies] = useState<MovieResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState<MovieResponse | null>(null);
  const [saving, setSaving] = useState(false);
  const [deleteConfirm, setDeleteConfirm] = useState<MovieResponse | null>(null);

  // Form state
  const [form, setForm] = useState({
    title: '',
    description: '',
    posterUrl: '',
    movieStatus: MovieStatus.DRAFT as string,
    durationInMinutes: '',
    releaseDate: '',
  });

  const loadMovies = useCallback(async () => {
    setLoading(true);
    try {
      const m = await movieApi.getAll();
      setMovies(m);
    } catch {
      addToast('error', 'Failed to load movies');
    } finally {
      setLoading(false);
    }
  }, [addToast]);

  useEffect(() => { loadMovies(); }, [loadMovies]);

  const filteredMovies = useMemo(() => {
    if (!search.trim()) return movies;
    const q = search.toLowerCase();
    return movies.filter(m => m.title.toLowerCase().includes(q));
  }, [movies, search]);

  const openCreate = () => {
    setEditing(null);
    setForm({ title: '', description: '', posterUrl: '', movieStatus: MovieStatus.DRAFT, durationInMinutes: '', releaseDate: '' });
    setModalOpen(true);
  };

  const openEdit = (movie: MovieResponse) => {
    setEditing(movie);
    setForm({
      title: movie.title,
      description: movie.description,
      posterUrl: movie.posterUrl,
      movieStatus: movie.movieStatus,
      durationInMinutes: String(movie.durationInMinutes),
      releaseDate: movie.releaseDate,
    });
    setModalOpen(true);
  };

  const handleSave = async () => {
    if (!form.title.trim() || !form.releaseDate) {
      addToast('warning', 'Title and release date are required');
      return;
    }

    setSaving(true);
    const payload: MovieCreateRequest | MovieUpdateRequest = {
      title: form.title.trim(),
      description: form.description.trim(),
      posterUrl: form.posterUrl.trim(),
      movieStatus: form.movieStatus as MovieStatus,
      durationInMinutes: Number(form.durationInMinutes) || 120,
      releaseDate: form.releaseDate,
    };

    try {
      if (editing) {
        await movieApi.update(editing.id, payload as MovieUpdateRequest);
        addToast('success', `"${form.title}" updated successfully`);
      } else {
        await movieApi.create(payload as MovieCreateRequest);
        addToast('success', `"${form.title}" created successfully`);
      }
      setModalOpen(false);
      loadMovies();
    } catch (err) {
      const msg = err instanceof ApiError ? err.message : 'Save failed';
      addToast('error', msg);
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (movie: MovieResponse) => {
    try {
      await movieApi.archive(movie.id);
      addToast('success', `"${movie.title}" archived`);
      setDeleteConfirm(null);
      loadMovies();
    } catch (err) {
      const msg = err instanceof ApiError ? err.message : 'Delete failed';
      addToast('error', msg);
    }
  };

  return (
    <div>
      <div className="cv-admin-page-header">
        <h1 className="cv-admin-page-title">Movies</h1>
        <div className="cv-admin-page-actions">
          <div className="cv-admin-search-box">
            <Search size={14} />
            <input
              type="text"
              placeholder="Search movies..."
              value={search}
              onChange={e => setSearch(e.target.value)}
            />
          </div>
          <Button variant="ghost" size="sm" onClick={loadMovies}>
            <RefreshCw size={14} />
          </Button>
          <Button size="sm" onClick={openCreate}>
            <Plus size={14} /> Add Movie
          </Button>
        </div>
      </div>

      {loading ? (
        <div className="cv-admin-loading">Loading movies...</div>
      ) : filteredMovies.length === 0 ? (
        <div className="cv-admin-empty">
          {search ? `No movies matching "${search}"` : 'No movies yet. Create your first movie!'}
        </div>
      ) : (
        <div className="cv-admin-table-wrapper">
          <table className="cv-admin-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Movie</th>
                <th>Status</th>
                <th>Duration</th>
                <th>Release Date</th>
                <th className="cv-admin-th-actions">Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredMovies.map(m => (
                <tr key={m.id}>
                  <td className="cv-admin-td-id">{m.id}</td>
                  <td>
                    <div className="cv-admin-movie-cell">
                      <img
                        className="cv-admin-movie-thumb"
                        src={m.posterUrl}
                        alt=""
                        onError={e => { (e.target as HTMLImageElement).src = `https://placehold.co/36x54/1a1a24/a78bfa?text=${encodeURIComponent(m.title.slice(0, 2))}`; }}
                      />
                      <span style={{ fontWeight: 600 }}>{m.title}</span>
                    </div>
                  </td>
                  <td>
                    <span className={`cv-admin-status cv-admin-status--${m.movieStatus?.toLowerCase()}`}>
                      {m.movieStatus}
                    </span>
                  </td>
                  <td>{m.durationInMinutes}m</td>
                  <td>{new Date(m.releaseDate).toLocaleDateString('en-IN')}</td>
                  <td>
                    <div className="cv-admin-row-actions">
                      <button className="cv-admin-action-btn cv-admin-action-btn--edit" onClick={() => openEdit(m)} title="Edit">
                        <Pencil size={14} />
                      </button>
                      <button className="cv-admin-action-btn cv-admin-action-btn--delete" onClick={() => setDeleteConfirm(m)} title="Archive">
                        <Trash2 size={14} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Create / Edit Modal */}
      <Modal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        title={editing ? 'Edit Movie' : 'Create Movie'}
        size="lg"
        footer={
          <div className="cv-admin-modal-footer">
            <Button variant="ghost" onClick={() => setModalOpen(false)}>Cancel</Button>
            <Button onClick={handleSave} loading={saving}>
              {editing ? 'Save Changes' : 'Create Movie'}
            </Button>
          </div>
        }
      >
        <div className="cv-admin-form">
          <Input
            label="Title"
            value={form.title}
            onChange={e => setForm(p => ({ ...p, title: e.target.value }))}
            placeholder="e.g. Inception"
          />
          <div className="cv-admin-form-row">
            <Input
              label="Duration (minutes)"
              type="number"
              value={form.durationInMinutes}
              onChange={e => setForm(p => ({ ...p, durationInMinutes: e.target.value }))}
              placeholder="120"
            />
            <Input
              label="Release Date"
              type="date"
              value={form.releaseDate}
              onChange={e => setForm(p => ({ ...p, releaseDate: e.target.value }))}
            />
          </div>
          <Select
            label="Status"
            value={form.movieStatus}
            onChange={e => setForm(p => ({ ...p, movieStatus: e.target.value }))}
            options={Object.values(MovieStatus).map(s => ({ value: s, label: s }))}
          />
          <Input
            label="Poster URL"
            value={form.posterUrl}
            onChange={e => setForm(p => ({ ...p, posterUrl: e.target.value }))}
            placeholder="https://..."
          />
          <div className="cv-input-group">
            <label className="cv-input-label">Description</label>
            <textarea
              className="cv-input cv-admin-textarea"
              value={form.description}
              onChange={e => setForm(p => ({ ...p, description: e.target.value }))}
              placeholder="Movie description..."
              rows={3}
            />
          </div>
        </div>
      </Modal>

      {/* Delete Confirmation Modal */}
      <Modal
        isOpen={!!deleteConfirm}
        onClose={() => setDeleteConfirm(null)}
        title="Archive Movie"
        footer={
          <div className="cv-admin-modal-footer">
            <Button variant="ghost" onClick={() => setDeleteConfirm(null)}>Cancel</Button>
            <Button variant="danger" onClick={() => deleteConfirm && handleDelete(deleteConfirm)}>
              Archive
            </Button>
          </div>
        }
      >
        <p className="cv-admin-confirm-text">
          Are you sure you want to archive <strong>"{deleteConfirm?.title}"</strong>? This will hide the movie from public listings.
        </p>
      </Modal>
    </div>
  );
}

/* ============================================================
   Admin Theatres — Full CRUD
   ============================================================ */
export function AdminTheatres() {
  const { addToast } = useToast();
  const [theatres, setTheatres] = useState<TheatreResponse[]>([]);
  const [cities, setCities] = useState<CityResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState<TheatreResponse | null>(null);
  const [saving, setSaving] = useState(false);
  const [deleteConfirm, setDeleteConfirm] = useState<TheatreResponse | null>(null);

  const [form, setForm] = useState({ name: '', address: '', cityId: '' });

  const loadData = useCallback(async () => {
    setLoading(true);
    try {
      const [t, c] = await Promise.all([theatreApi.getAll(), cityApi.getAll()]);
      setTheatres(t);
      setCities(c);
    } catch {
      addToast('error', 'Failed to load theatres');
    } finally {
      setLoading(false);
    }
  }, [addToast]);

  useEffect(() => { loadData(); }, [loadData]);

  const cityMap = useMemo(() => new Map(cities.map(c => [c.id, c])), [cities]);

  const filteredTheatres = useMemo(() => {
    if (!search.trim()) return theatres;
    const q = search.toLowerCase();
    return theatres.filter(t =>
      t.name.toLowerCase().includes(q) ||
      t.address.toLowerCase().includes(q) ||
      (cityMap.get(t.cityId)?.name || '').toLowerCase().includes(q)
    );
  }, [theatres, search, cityMap]);

  const openCreate = () => {
    setEditing(null);
    setForm({ name: '', address: '', cityId: cities.length > 0 ? String(cities[0].id) : '' });
    setModalOpen(true);
  };

  const openEdit = (theatre: TheatreResponse) => {
    setEditing(theatre);
    setForm({ name: theatre.name, address: theatre.address, cityId: String(theatre.cityId) });
    setModalOpen(true);
  };

  const handleSave = async () => {
    if (!form.name.trim() || !form.cityId) {
      addToast('warning', 'Name and city are required');
      return;
    }

    setSaving(true);
    const payload: TheatreCreateRequest | TheatreUpdateRequest = {
      name: form.name.trim(),
      address: form.address.trim(),
      cityId: Number(form.cityId),
    };

    try {
      if (editing) {
        await theatreApi.update(editing.id, payload as TheatreUpdateRequest);
        addToast('success', `"${form.name}" updated successfully`);
      } else {
        await theatreApi.create(payload as TheatreCreateRequest);
        addToast('success', `"${form.name}" created successfully`);
      }
      setModalOpen(false);
      loadData();
    } catch (err) {
      const msg = err instanceof ApiError ? err.message : 'Save failed';
      addToast('error', msg);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div>
      <div className="cv-admin-page-header">
        <h1 className="cv-admin-page-title">Theatres</h1>
        <div className="cv-admin-page-actions">
          <div className="cv-admin-search-box">
            <Search size={14} />
            <input
              type="text"
              placeholder="Search theatres..."
              value={search}
              onChange={e => setSearch(e.target.value)}
            />
          </div>
          <Button variant="ghost" size="sm" onClick={loadData}>
            <RefreshCw size={14} />
          </Button>
          <Button size="sm" onClick={openCreate}>
            <Plus size={14} /> Add Theatre
          </Button>
        </div>
      </div>

      {loading ? (
        <div className="cv-admin-loading">Loading theatres...</div>
      ) : filteredTheatres.length === 0 ? (
        <div className="cv-admin-empty">
          {search ? `No theatres matching "${search}"` : 'No theatres yet. Add your first theatre!'}
        </div>
      ) : (
        <div className="cv-admin-table-wrapper">
          <table className="cv-admin-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Address</th>
                <th>City</th>
                <th className="cv-admin-th-actions">Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredTheatres.map(t => (
                <tr key={t.id}>
                  <td className="cv-admin-td-id">{t.id}</td>
                  <td style={{ fontWeight: 600 }}>{t.name}</td>
                  <td>{t.address}</td>
                  <td>
                    <span className="cv-admin-city-badge">
                      <MapPin size={11} />
                      {cityMap.get(t.cityId)?.name || t.cityId}
                    </span>
                  </td>
                  <td>
                    <div className="cv-admin-row-actions">
                      <button className="cv-admin-action-btn cv-admin-action-btn--edit" onClick={() => openEdit(t)} title="Edit">
                        <Pencil size={14} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Create / Edit Modal */}
      <Modal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        title={editing ? 'Edit Theatre' : 'Create Theatre'}
        footer={
          <div className="cv-admin-modal-footer">
            <Button variant="ghost" onClick={() => setModalOpen(false)}>Cancel</Button>
            <Button onClick={handleSave} loading={saving}>
              {editing ? 'Save Changes' : 'Create Theatre'}
            </Button>
          </div>
        }
      >
        <div className="cv-admin-form">
          <Input
            label="Theatre Name"
            value={form.name}
            onChange={e => setForm(p => ({ ...p, name: e.target.value }))}
            placeholder="e.g. PVR Cinemas"
          />
          <Input
            label="Address"
            value={form.address}
            onChange={e => setForm(p => ({ ...p, address: e.target.value }))}
            placeholder="e.g. MG Road, Sector 5"
          />
          <Select
            label="City"
            value={form.cityId}
            onChange={e => setForm(p => ({ ...p, cityId: e.target.value }))}
            options={cities.map(c => ({ value: c.id, label: `${c.name}, ${c.state}` }))}
            placeholder="Select a city"
          />
        </div>
      </Modal>
    </div>
  );
}

/* ============================================================
   Admin Shows — Full CRUD
   ============================================================ */
export function AdminShows() {
  const { addToast } = useToast();
  const [shows, setShows] = useState<ShowResponse[]>([]);
  const [movies, setMovies] = useState<MovieResponse[]>([]);
  const [screens, setScreens] = useState<ScreenResponse[]>([]);
  const [theatres, setTheatres] = useState<TheatreResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState<ShowResponse | null>(null);
  const [saving, setSaving] = useState(false);
  const [deleteConfirm, setDeleteConfirm] = useState<ShowResponse | null>(null);

  const [form, setForm] = useState({
    movieId: '', screenId: '', showDate: '', ticketPrice: '', startTime: '', endTime: '',
  });

  const loadData = useCallback(async () => {
    setLoading(true);
    try {
      const [s, m, sc, t] = await Promise.all([
        showApi.getAll(),
        movieApi.getAll(),
        screenApi.getAll(),
        theatreApi.getAll(),
      ]);
      setShows(s);
      setMovies(m);
      setScreens(sc);
      setTheatres(t);
    } catch {
      addToast('error', 'Failed to load shows');
    } finally {
      setLoading(false);
    }
  }, [addToast]);

  useEffect(() => { loadData(); }, [loadData]);

  const movieMap = useMemo(() => new Map(movies.map(m => [m.id, m])), [movies]);
  const screenMap = useMemo(() => new Map(screens.map(s => [s.id, s])), [screens]);
  const theatreMap = useMemo(() => new Map(theatres.map(t => [t.id, t])), [theatres]);

  const filteredShows = useMemo(() => {
    const list = shows.slice(0, 200); // Show first 200
    if (!search.trim()) return list;
    const q = search.toLowerCase();
    return list.filter(s => {
      const movieName = movieMap.get(s.movieId)?.title || '';
      return movieName.toLowerCase().includes(q);
    });
  }, [shows, search, movieMap]);

  const formatTime = (time: string) => {
    const [h, m] = time.split(':').map(Number);
    const ampm = h >= 12 ? 'PM' : 'AM';
    const h12 = h % 12 || 12;
    return `${h12}:${String(m).padStart(2, '0')} ${ampm}`;
  };

  const getScreenLabel = (screenId: number) => {
    const screen = screenMap.get(screenId);
    if (!screen) return `Screen #${screenId}`;
    const theatre = theatreMap.get(screen.theatreId);
    return `${theatre?.name || ''} — ${screen.name}`;
  };

  const openCreate = () => {
    setEditing(null);
    setForm({
      movieId: movies.length > 0 ? String(movies[0].id) : '',
      screenId: screens.length > 0 ? String(screens[0].id) : '',
      showDate: '',
      ticketPrice: '200',
      startTime: '10:00',
      endTime: '12:30',
    });
    setModalOpen(true);
  };

  const openEdit = (show: ShowResponse) => {
    setEditing(show);
    setForm({
      movieId: String(show.movieId),
      screenId: String(show.screenId),
      showDate: show.showDate,
      ticketPrice: String(show.ticketPrice),
      startTime: show.startTime.slice(0, 5),
      endTime: show.endTime.slice(0, 5),
    });
    setModalOpen(true);
  };

  const handleSave = async () => {
    if (!form.movieId || !form.screenId || !form.showDate || !form.startTime || !form.endTime) {
      addToast('warning', 'All fields are required');
      return;
    }

    setSaving(true);
    const payload: ShowCreateRequest | ShowUpdateRequest = {
      movieId: Number(form.movieId),
      screenId: Number(form.screenId),
      showDate: form.showDate,
      ticketPrice: Number(form.ticketPrice) || 200,
      startTime: form.startTime + ':00',
      endTime: form.endTime + ':00',
    };

    try {
      if (editing) {
        await showApi.update(editing.id, payload as ShowUpdateRequest);
        addToast('success', 'Show updated successfully');
      } else {
        await showApi.create(payload as ShowCreateRequest);
        addToast('success', 'Show created successfully');
      }
      setModalOpen(false);
      loadData();
    } catch (err) {
      const msg = err instanceof ApiError ? err.message : 'Save failed';
      addToast('error', msg);
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async (show: ShowResponse) => {
    try {
      await showApi.delete(show.id);
      addToast('success', 'Show deleted');
      setDeleteConfirm(null);
      loadData();
    } catch (err) {
      const msg = err instanceof ApiError ? err.message : 'Delete failed';
      addToast('error', msg);
    }
  };

  const screenOptions = useMemo(() => {
    return screens.map(s => {
      const theatre = theatreMap.get(s.theatreId);
      return {
        value: s.id,
        label: `${theatre?.name || 'Unknown'} — ${s.name}`,
      };
    });
  }, [screens, theatreMap]);

  return (
    <div>
      <div className="cv-admin-page-header">
        <h1 className="cv-admin-page-title">Shows</h1>
        <div className="cv-admin-page-actions">
          <div className="cv-admin-search-box">
            <Search size={14} />
            <input
              type="text"
              placeholder="Search by movie..."
              value={search}
              onChange={e => setSearch(e.target.value)}
            />
          </div>
          <Button variant="ghost" size="sm" onClick={loadData}>
            <RefreshCw size={14} />
          </Button>
          <Button size="sm" onClick={openCreate}>
            <Plus size={14} /> Add Show
          </Button>
        </div>
      </div>

      {loading ? (
        <div className="cv-admin-loading">Loading shows...</div>
      ) : filteredShows.length === 0 ? (
        <div className="cv-admin-empty">
          {search ? `No shows matching "${search}"` : 'No shows yet. Schedule your first show!'}
        </div>
      ) : (
        <div className="cv-admin-table-wrapper">
          <table className="cv-admin-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Movie</th>
                <th>Screen</th>
                <th>Date</th>
                <th>Time</th>
                <th>Price</th>
                <th className="cv-admin-th-actions">Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredShows.map(s => (
                <tr key={s.id}>
                  <td className="cv-admin-td-id">{s.id}</td>
                  <td style={{ fontWeight: 600 }}>{movieMap.get(s.movieId)?.title || s.movieId}</td>
                  <td>
                    <span className="cv-admin-screen-badge">{getScreenLabel(s.screenId)}</span>
                  </td>
                  <td>{new Date(s.showDate).toLocaleDateString('en-IN', { weekday: 'short', month: 'short', day: 'numeric' })}</td>
                  <td>
                    <span className="cv-admin-time-badge">{formatTime(s.startTime)}</span>
                  </td>
                  <td className="cv-admin-td-price">₹{s.ticketPrice}</td>
                  <td>
                    <div className="cv-admin-row-actions">
                      <button className="cv-admin-action-btn cv-admin-action-btn--edit" onClick={() => openEdit(s)} title="Edit">
                        <Pencil size={14} />
                      </button>
                      <button className="cv-admin-action-btn cv-admin-action-btn--delete" onClick={() => setDeleteConfirm(s)} title="Delete">
                        <Trash2 size={14} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Create / Edit Modal */}
      <Modal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        title={editing ? 'Edit Show' : 'Schedule Show'}
        size="lg"
        footer={
          <div className="cv-admin-modal-footer">
            <Button variant="ghost" onClick={() => setModalOpen(false)}>Cancel</Button>
            <Button onClick={handleSave} loading={saving}>
              {editing ? 'Save Changes' : 'Create Show'}
            </Button>
          </div>
        }
      >
        <div className="cv-admin-form">
          <Select
            label="Movie"
            value={form.movieId}
            onChange={e => setForm(p => ({ ...p, movieId: e.target.value }))}
            options={movies.map(m => ({ value: m.id, label: m.title }))}
            placeholder="Select a movie"
          />
          <Select
            label="Screen"
            value={form.screenId}
            onChange={e => setForm(p => ({ ...p, screenId: e.target.value }))}
            options={screenOptions}
            placeholder="Select a screen"
          />
          <div className="cv-admin-form-row">
            <Input
              label="Show Date"
              type="date"
              value={form.showDate}
              onChange={e => setForm(p => ({ ...p, showDate: e.target.value }))}
            />
            <Input
              label="Ticket Price (₹)"
              type="number"
              value={form.ticketPrice}
              onChange={e => setForm(p => ({ ...p, ticketPrice: e.target.value }))}
              placeholder="200"
            />
          </div>
          <div className="cv-admin-form-row">
            <Input
              label="Start Time"
              type="time"
              value={form.startTime}
              onChange={e => setForm(p => ({ ...p, startTime: e.target.value }))}
            />
            <Input
              label="End Time"
              type="time"
              value={form.endTime}
              onChange={e => setForm(p => ({ ...p, endTime: e.target.value }))}
            />
          </div>
        </div>
      </Modal>

      {/* Delete Confirmation Modal */}
      <Modal
        isOpen={!!deleteConfirm}
        onClose={() => setDeleteConfirm(null)}
        title="Delete Show"
        footer={
          <div className="cv-admin-modal-footer">
            <Button variant="ghost" onClick={() => setDeleteConfirm(null)}>Cancel</Button>
            <Button variant="danger" onClick={() => deleteConfirm && handleDelete(deleteConfirm)}>
              Delete
            </Button>
          </div>
        }
      >
        <p className="cv-admin-confirm-text">
          Are you sure you want to delete this show for <strong>"{deleteConfirm ? movieMap.get(deleteConfirm.movieId)?.title : ''}"</strong>? This action cannot be undone.
        </p>
      </Modal>
    </div>
  );
}
