import React, { useEffect, useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { api } from './services/api';
import './styles.css';

export const fallbackWorkers = [
  { id: 1, name: 'Raj Kumar', role: 'Electrician', rating: 4.8, reviews: 142, rate: 499, distance: 1.8, exp: 6, photo: 'RK', color: '#0f62fe', available: true, skills: ['Wiring & Fuse', 'Appliance Repair', 'Inverter Setup', 'AC Installation'] },
  { id: 2, name: 'Sanjay Verma', role: 'Plumber', rating: 4.9, reviews: 98, rate: 450, distance: 2.3, exp: 8, photo: 'SV', color: '#00a676', available: true, skills: ['Leak Repairs', 'Pipe Fittings', 'Tap Installation', 'Drain Unclog'] },
  { id: 3, name: 'Amit Singh', role: 'Carpenter', rating: 4.7, reviews: 88, rate: 599, distance: 3.1, exp: 7, photo: 'AS', color: '#f59e0b', available: true, skills: ['Custom Furniture', 'Door & Window Locks', 'Wood Polish', 'Modular Wardrobe'] },
  { id: 4, name: 'Priya Sharma', role: 'Home Cleaning', rating: 4.9, reviews: 215, rate: 399, distance: 2.5, exp: 5, photo: 'PS', color: '#9333ea', available: true, skills: ['Deep House Cleaning', 'Kitchen & Bathroom Sanitization', 'Sofa Shampoo'] }
];

export const fallbackServices = [
  { id: 1, name: 'Electrician', description: 'Complete wiring, switchboards, lights & fans', category: 'Electrical', basePrice: 149 },
  { id: 2, name: 'Plumber', description: 'Pipe leakage, tap repairs & sanitary fittings', category: 'Plumbing', basePrice: 149 },
  { id: 3, name: 'Carpenter', description: 'Furniture making, woodwork & hinge repairs', category: 'Carpentry', basePrice: 199 },
  { id: 4, name: 'Home Cleaning', description: 'Deep home, kitchen, and bathroom sanitization', category: 'Cleaning', basePrice: 299 },
  { id: 5, name: 'AC Service & Repair', description: 'Deep foam jet service & gas refilling', category: 'Appliances', basePrice: 399 },
  { id: 6, name: 'Painter', description: 'Interior/exterior wall painting & waterproofing', category: 'Painting', basePrice: 499 },
  { id: 7, name: 'Vehicle Mechanic', description: 'Two & four-wheeler servicing & battery jumpstart', category: 'Automotive', basePrice: 249 },
  { id: 8, name: 'Gardening & Lawn Care', description: 'Plant pruning, pest control & lawn mowing', category: 'Outdoor', basePrice: 199 }
];

export const serviceIcons = {
  'Electrician': '⚡',
  'Plumber': '🔧',
  'Carpenter': '🪚',
  'Home Cleaning': '✨',
  'AC Service & Repair': '❄️',
  'Painter': '🎨',
  'Vehicle Mechanic': '🚗',
  'Gardening & Lawn Care': '🌱'
};

export const CITIES = ['New Delhi (NCR)', 'Mumbai', 'Bengaluru', 'Hyderabad', 'Pune', 'Chennai'];

// Toast Notification Manager
let addToastExternal = null;
export function showToast(message, type = 'info') {
  if (addToastExternal) addToastExternal(message, type);
}

export function ToastContainer() {
  const [toasts, setToasts] = useState([]);

  useEffect(() => {
    addToastExternal = (msg, type) => {
      const id = Date.now();
      setToasts(prev => [...prev, { id, msg, type }]);
      setTimeout(() => {
        setToasts(prev => prev.filter(t => t.id !== id));
      }, 3500);
    };
  }, []);

  return (
    <div className="toast-container">
      {toasts.map(t => (
        <div key={t.id} className="toast">
          <span>{t.type === 'success' ? '✅' : t.type === 'error' ? '⚠️' : 'ℹ️'}</span>
          <span>{t.msg}</span>
        </div>
      ))}
    </div>
  );
}

export function Avatar({ worker, large = false }) {
  return (
    <div className={large ? 'profile-avatar-lg' : 'worker-avatar'} style={{ background: worker?.color || '#0f62fe' }}>
      {worker?.photo || worker?.name?.slice(0, 2)?.toUpperCase() || 'WS'}
    </div>
  );
}

export function Stars({ value, count }) {
  return (
    <span className="worker-rating-badge">
      <span>★</span> {Number(value || 0).toFixed(1)} {count !== undefined && <small className="muted">({count})</small>}
    </span>
  );
}

/* =========================================
   HEADER & NAVBAR
========================================= */
export function Header({ page, setPage, role, setRole, auth, onLogout, city, setCity }) {
  return (
    <header className="app-header">
      <div className="brand" onClick={() => setPage('home')} style={{ cursor: 'pointer' }}>
        <div className="brand-icon">W</div>
        <div>Work<span>Saathi</span></div>
      </div>

      <div className="header-center">
        <div className="city-badge">
          <span>📍</span>
          <select
            value={city}
            onChange={e => setCity(e.target.value)}
            style={{ border: 'none', background: 'transparent', fontWeight: 600, outline: 'none', cursor: 'pointer' }}
          >
            {CITIES.map(c => <option key={c} value={c}>{c}</option>)}
          </select>
        </div>

        <nav className="nav-links">
          <button className={`nav-item ${page === 'find' ? 'active' : ''}`} onClick={() => setPage('find')}>Explore Pros</button>
          <button className={`nav-item ${page === 'how' ? 'active' : ''}`} onClick={() => setPage('how')}>How It Works</button>
          <button className={`nav-item ${page === 'about' ? 'active' : ''}`} onClick={() => setPage('about')}>Trust & Safety</button>
        </nav>
      </div>

      <div className="header-actions">
        {auth ? (
          <>
            <button className="btn btn-outline btn-sm" onClick={() => setPage('dashboard')}>
              👤 {auth.name} ({auth.role})
            </button>
            <button className="btn btn-danger-outline btn-sm" onClick={onLogout}>Log out</button>
          </>
        ) : (
          <>
            <button className="btn btn-outline" onClick={() => setPage('auth')}>Log in</button>
            <button className="btn btn-primary" onClick={() => setPage('auth')}>Book a Pro ➔</button>
          </>
        )}

        <select
          aria-label="View role"
          className="role-selector"
          value={role}
          onChange={e => {
            const nextRole = e.target.value;
            setRole(nextRole);
            setPage('dashboard');
          }}
        >
          <option value="customer">👨 Customer Portal</option>
          <option value="worker">⚡ Worker Portal</option>
          <option value="admin">🛡️ Admin Portal</option>
        </select>
      </div>
    </header>
  );
}

/* =========================================
   HOME PAGE
========================================= */
export function Home({ setPage, setQuery, servicesList = [] }) {
  const displayServices = servicesList.length ? servicesList : fallbackServices;

  return (
    <>
      <section className="hero-wrapper">
        <div className="hero-container">
          <div className="hero-copy">
            <div className="hero-badge">✦ INDIA'S VERIFIED ON-DEMAND HOME SERVICES</div>
            <h1 className="hero-title">
              Expert Help for<br />
              <em>Every Home</em> Need.
            </h1>
            <p className="hero-desc">
              Book certified electricians, plumbers, carpenters, and appliance experts with background checks, fixed quotes, and satisfaction guarantee.
            </p>

            <div className="search-box-elevated">
              <span className="search-icon">🔍</span>
              <input
                className="search-input"
                aria-label="Search a service"
                placeholder="What service do you need help with today?"
                onChange={e => setQuery(e.target.value)}
                onKeyDown={e => e.key === 'Enter' && setPage('find')}
              />
              <button className="btn btn-primary" onClick={() => setPage('find')}>
                Find Pro ➔
              </button>
            </div>

            <div className="popular-chips">
              <span>Popular:</span>
              {['Electrician', 'Plumber', 'Home Cleaning', 'AC Service & Repair'].map(x => (
                <button key={x} className="popular-chip" onClick={() => { setQuery(x); setPage('find'); }}>
                  {x}
                </button>
              ))}
            </div>
          </div>

          <div className="hero-visual-card">
            <div className="hero-visual-glow"></div>
            <div className="hero-visual-bottom-glow"></div>
            <div className="hero-card-header">
              <h3>Live Verified Pros Near You</h3>
              <p>Average arrival time: under 30 minutes in your sector</p>
            </div>

            <div className="pro-spotlight-list">
              {fallbackWorkers.slice(0, 3).map(w => (
                <div key={w.id} className="spotlight-row">
                  <div className="spotlight-avatar" style={{ background: w.color }}>{w.photo}</div>
                  <div className="spotlight-info">
                    <b>{w.name} <span className="verified-icon">✓</span></b>
                    <span>{w.role} · ★ {w.rating} ({w.reviews} jobs)</span>
                  </div>
                  <span className="spotlight-badge">Available Now</span>
                </div>
              ))}
            </div>

            <div className="hero-guarantee">
              <span>🛡️</span>
              <div>
                <b>WorkSaathi Cover Guarantee</b>
                <p>₹10,000 damage protection on every verified booking.</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* TRUST METRICS STRIP */}
      <section className="trust-metrics-strip">
        <div className="trust-metrics-container">
          <div className="metric-box">
            <div className="metric-icon">🛡️</div>
            <div className="metric-content">
              <strong>15,000+</strong>
              <span>Background-Checked Pros</span>
            </div>
          </div>
          <div className="metric-box">
            <div className="metric-icon">⭐</div>
            <div className="metric-content">
              <strong>4.88 / 5</strong>
              <span>Average Customer Rating</span>
            </div>
          </div>
          <div className="metric-box">
            <div className="metric-icon">⏱️</div>
            <div className="metric-content">
              <strong>30 Min</strong>
              <span>Express Arrival Option</span>
            </div>
          </div>
          <div className="metric-box">
            <div className="metric-icon">📍</div>
            <div className="metric-content">
              <strong>25+ Cities</strong>
              <span>Delhi-NCR, Mumbai, BLR & more</span>
            </div>
          </div>
        </div>
      </section>

      {/* SERVICES SECTION */}
      <section className="section">
        <div className="section-head">
          <div>
            <div className="eyebrow-text">BROWSE OUR SERVICES</div>
            <h2 className="section-title">Whatever needs doing,<br /><em>we have the specialist.</em></h2>
          </div>
          <button className="btn btn-outline" onClick={() => setPage('find')}>Explore All Services ➔</button>
        </div>

        <div className="services-grid">
          {displayServices.map(s => {
            const icon = serviceIcons[s.name] || '🛠️';
            return (
              <div
                key={s.id || s.name}
                className="service-card-modern"
                onClick={() => { setQuery(s.name); setPage('find'); }}
                style={{ cursor: 'pointer' }}
              >
                <div className="service-card-top">
                  <div className="service-emoji">{icon}</div>
                  <span className="service-price-pill">Starts ₹{s.basePrice || 149}</span>
                </div>
                <div className="service-card-info">
                  <h3>{s.name}</h3>
                  <p>{s.description || 'Verified on-demand doorstep service by certified local professionals.'}</p>
                </div>
                <div className="service-card-foot">
                  <span>Book Inspection</span>
                  <span>➔</span>
                </div>
              </div>
            );
          })}
        </div>
      </section>

      {/* HOW IT WORKS */}
      <section className="section how-section">
        <div className="section-head" style={{ textAlign: 'center', justifyContent: 'center', flexDirection: 'column' }}>
          <div className="eyebrow-text">TRANSPARENT & HASSLE-FREE</div>
          <h2 className="section-title">How WorkSaathi Works in 3 Simple Steps</h2>
        </div>

        <div className="steps-container">
          <div className="step-card">
            <div className="step-num">01</div>
            <h3>Choose Service & Schedule</h3>
            <p>Select your required task, describe the issue, and pick a convenient date & time slot.</p>
          </div>
          <div className="step-card">
            <div className="step-num">02</div>
            <h3>Matched with Top Pro</h3>
            <p>Our intelligent matching pairs you with a top-rated, police-verified specialist nearby.</p>
          </div>
          <div className="step-card">
            <div className="step-num">03</div>
            <h3>Track & Pay After Completion</h3>
            <p>Track your technician in real-time, inspect their work, and pay securely via UPI or cash.</p>
          </div>
        </div>
      </section>

      {/* CTA SECTION */}
      <section className="section">
        <div style={{ background: 'linear-gradient(135deg, #0f62fe 0%, #0043ce 100%)', borderRadius: '20px', padding: '50px 40px', color: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: '30px', flexWrap: 'wrap' }}>
          <div>
            <h2 style={{ fontFamily: 'Fraunces, serif', fontSize: '36px', marginBottom: '12px' }}>Are you a skilled professional?</h2>
            <p style={{ fontSize: '16px', color: '#dbeafe', maxWidth: '500px' }}>Join thousands of electricians, plumbers, and technicians earning ₹40,000+ monthly with flexible hours and weekly payouts.</p>
          </div>
          <button className="btn btn-secondary btn-lg" onClick={() => setPage('auth')}>
            Partner with WorkSaathi ➔
          </button>
        </div>
      </section>

      <Footer />
    </>
  );
}

/* =========================================
   FIND WORKERS
========================================= */
export function FindWorkers({ query, setQuery, setPage, setSelected, workerList = [], servicesList = [], loading, error, city }) {
  const [onlyAvailable, setOnlyAvailable] = useState(false);
  const [selectedService, setSelectedService] = useState('All');
  const [sort, setSort] = useState('Recommended');
  const [priceMax, setPriceMax] = useState(1500);

  const result = useMemo(() => {
    return workerList.filter(w => {
      const matchAvail = !onlyAvailable || w.available;
      const matchService = selectedService === 'All' || (w.skills && w.skills.some(s => s.toLowerCase().includes(selectedService.toLowerCase()))) || w.role === selectedService;
      const matchPrice = (w.rate || 500) <= priceMax;
      const matchQuery = !query || `${w.role} ${w.name} ${(w.skills || []).join(' ')}`.toLowerCase().includes(query.toLowerCase());
      return matchAvail && matchService && matchPrice && matchQuery;
    }).sort((a, b) => {
      if (sort === 'Rating') return b.rating - a.rating;
      if (sort === 'Price: Low to High') return a.rate - b.rate;
      if (sort === 'Price: High to Low') return b.rate - a.rate;
      return a.distance - b.distance;
    });
  }, [query, onlyAvailable, selectedService, sort, priceMax, workerList]);

  return (
    <main className="finder-page">
      <div className="finder-header">
        <div className="breadcrumb">Home / Find Professionals / {city}</div>
        <h1>Certified Professionals in <em>{city}</em></h1>
      </div>

      <div className="finder-controls">
        <div className="finder-search-bar">
          <span>🔍</span>
          <input
            value={query}
            onChange={e => setQuery(e.target.value)}
            placeholder="Search by specialty (e.g. Electrician, RO Water Repair, Modular Kitchen)..."
          />
        </div>
        <button className="btn btn-primary" onClick={() => {}}>Search</button>
      </div>

      <div className="finder-layout">
        <aside className="finder-sidebar">
          <div className="filter-header">
            <b>Filters</b>
            <button className="btn btn-sm btn-outline" onClick={() => { setQuery(''); setSelectedService('All'); setOnlyAvailable(false); setPriceMax(1500); }}>Reset</button>
          </div>

          <div className="filter-group">
            <label>Service Category</label>
            <select className="filter-select" value={selectedService} onChange={e => setSelectedService(e.target.value)}>
              <option value="All">All Categories</option>
              {servicesList.map(s => <option key={s.id} value={s.name}>{s.name}</option>)}
            </select>
          </div>

          <div className="filter-group">
            <label>Max Budget: ₹{priceMax}</label>
            <input
              type="range"
              min="200"
              max="2000"
              step="50"
              value={priceMax}
              onChange={e => setPriceMax(Number(e.target.value))}
              style={{ width: '100%', accentColor: 'var(--primary)' }}
            />
          </div>

          <div className="filter-group">
            <label className="checkbox-label">
              <input type="checkbox" checked={onlyAvailable} onChange={e => setOnlyAvailable(e.target.checked)} />
              Available for Instant Visit
            </label>
            <label className="checkbox-label">
              <input type="checkbox" defaultChecked readOnly />
              100% Background Verified Only
            </label>
          </div>
        </aside>

        <div className="worker-results-column">
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
            <span style={{ fontSize: '14px', fontWeight: 600, color: 'var(--slate)' }}>
              Showing <b>{result.length} certified professionals</b> in {city}
            </span>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '13px' }}>
              <span>Sort by:</span>
              <select className="filter-select" style={{ width: 'auto' }} value={sort} onChange={e => setSort(e.target.value)}>
                <option>Recommended</option>
                <option>Rating</option>
                <option>Price: Low to High</option>
                <option>Price: High to Low</option>
              </select>
            </div>
          </div>

          {loading ? (
            <div className="worker-result-card" style={{ justifyContent: 'center', padding: '40px' }}>
              <b>Loading live verified specialists...</b>
            </div>
          ) : result.length === 0 ? (
            <div className="worker-result-card" style={{ flexDirection: 'column', textAlign: 'center', padding: '50px' }}>
              <h3>No professionals match your current filters</h3>
              <p style={{ color: 'var(--muted)', margin: '8px 0 16px' }}>Try widening your price range or search terms.</p>
              <button className="btn btn-primary btn-sm" onClick={() => { setQuery(''); setSelectedService('All'); setOnlyAvailable(false); }}>Reset Filters</button>
            </div>
          ) : (
            <div className="worker-list-results">
              {result.map(w => (
                <article className="worker-result-card" key={w.id}>
                  <Avatar worker={w} />
                  <div className="worker-main">
                    <h3>{w.name} <span className="verified-icon">✓</span></h3>
                    <div className="worker-meta-line">{w.role} · {w.exp || 4}+ years experience · {w.distance ? `${w.distance.toFixed(1)} km away` : 'Near you'}</div>
                    <div><Stars value={w.rating} count={w.reviews} /></div>
                    <div className="worker-skills-tags">
                      {(w.skills || []).slice(0, 4).map(s => <span key={s} className="skill-tag">{s}</span>)}
                    </div>
                  </div>
                  <div className="worker-side-action">
                    <div className={`avail-badge ${w.available ? 'available' : 'booked'}`}>
                      ● {w.available ? 'Available Today' : 'Booked'}
                    </div>
                    <div className="worker-price-tag">₹{w.rate}<small> / visit</small></div>
                    <button className="btn btn-primary btn-sm" onClick={() => { setSelected(w); setPage('profile'); }}>
                      View Profile & Book
                    </button>
                  </div>
                </article>
              ))}
            </div>
          )}
        </div>
      </div>
    </main>
  );
}

/* =========================================
   WORKER PROFILE PAGE
========================================= */
export function Profile({ worker, setPage, setBooking }) {
  return (
    <main className="profile-page">
      <button className="btn btn-outline btn-sm" onClick={() => setPage('find')} style={{ marginBottom: '24px' }}>
        ← Back to Search
      </button>

      <div className="profile-layout">
        <div className="profile-main-card">
          <div className="profile-header-strip">
            <Avatar worker={worker} large />
            <div className="profile-title-area">
              <h1>{worker.name} <span className="verified-icon">✓</span></h1>
              <p style={{ color: 'var(--slate)', fontSize: '15px' }}>{worker.role} Specialist · {worker.exp || 5}+ Years Field Experience</p>
              <div style={{ marginTop: '6px' }}><Stars value={worker.rating} count={worker.reviews} /></div>
            </div>
          </div>

          <div className="profile-section-block">
            <h3>About {worker.name}</h3>
            <p>{worker.bio || `Certified and background-verified ${worker.role.toLowerCase()} with extensive experience across residential societies and commercial facilities. Known for prompt diagnostic accuracy, neat cable routing, and transparent billing.`}</p>
          </div>

          <div className="profile-section-block">
            <h3>Specialized Skills & Toolsets</h3>
            <div className="worker-skills-tags">
              {(worker.skills || []).map(s => <span key={s} className="skill-tag" style={{ padding: '6px 12px', fontSize: '13px' }}>{s}</span>)}
            </div>
          </div>

          <div className="profile-section-block">
            <h3>Verified Customer Testimonials</h3>
            <div style={{ background: 'var(--light-bg)', padding: '18px', borderRadius: 'var(--radius-sm)', border: '1px solid var(--border)', marginTop: '12px' }}>
              <p style={{ fontStyle: 'italic', color: 'var(--dark)', fontSize: '14px' }}>
                “Arrived right on time within 25 minutes of booking. Inspected the circuit board, replaced the faulty MCB, and charged exactly the estimated amount. Very respectful and tidy work.”
              </p>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '10px', fontSize: '12px', color: 'var(--muted)' }}>
                <b>— Rohit Malhotra, Verified Resident</b>
                <span>★★★★★ 5.0</span>
              </div>
            </div>
          </div>
        </div>

        <aside className="booking-sticky-card">
          <span className="eyebrow-text">STANDARD INSPECTION</span>
          <div className="sticky-price">₹{worker.rate}<small> / visit</small></div>
          <p style={{ fontSize: '13px', color: 'var(--muted)' }}>Transparent upfront pricing with zero hidden cancellation fees.</p>
          
          <button className="btn btn-primary btn-lg" style={{ width: '100%', marginTop: '18px' }} onClick={() => setBooking(true)}>
            Schedule Service Now ➔
          </button>

          <div className="booking-perks">
            <div>✓ <b>WorkSaathi Shield</b>: ₹10,000 damage cover</div>
            <div>✓ <b>Pay after service</b> (UPI, Cash, or Card)</div>
            <div>✓ <b>30-Day Workmanship Warranty</b></div>
          </div>
        </aside>
      </div>
    </main>
  );
}

/* =========================================
   INTERACTIVE BOOKING MODAL
========================================= */
export function Booking({ worker, close, setPage, services = [], auth, onJobCreated }) {
  const [step, setStep] = useState(1);
  const [message, setMessage] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [coupon, setCoupon] = useState('');
  const [discount, setDiscount] = useState(0);

  const matchedService = services.find(s => (worker.skills || []).includes(s.name)) || services[0];
  const [details, setDetails] = useState({
    serviceId: matchedService?.id || (services[0]?.id || 1),
    description: '',
    address: 'Flat 302, Palm Heights, Sector 45, Gurgaon',
    date: new Date(Date.now() + 86400000).toISOString().split('T')[0],
    time: '10:00 AM'
  });

  const applyCoupon = () => {
    if (coupon.trim().toUpperCase() === 'FIRST100') {
      setDiscount(100);
      showToast('Coupon FIRST100 applied! ₹100 discount added.', 'success');
    } else {
      showToast('Invalid coupon code. Try FIRST100', 'error');
    }
  };

  const finalAmount = Math.max(99, (worker.rate || 499) - discount + 49); // +49 safety fee

  const handleConfirm = async () => {
    if (!auth) {
      setMessage('Please log in before sending a service booking.');
      return;
    }
    setSubmitting(true);
    setMessage('');
    try {
      await api.createJob({
        workerId: worker.id,
        serviceId: Number(details.serviceId),
        title: `${worker.role} Service Booking`,
        description: details.description || 'Standard service request via WorkSaathi platform',
        scheduledDate: `${details.date}T10:00:00`,
        scheduledTime: details.time,
        address: details.address,
        estimatedPrice: finalAmount
      });
      showToast('Booking submitted successfully! Specialist notified.', 'success');
      if (onJobCreated) onJobCreated();
      close();
      setPage('dashboard');
    } catch (e) {
      setMessage(e.message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-window">
        <button className="close-btn" onClick={close}>✕</button>

        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '20px' }}>
          <div style={{ background: 'var(--primary-light)', color: 'var(--primary)', padding: '6px 12px', borderRadius: '20px', fontSize: '12px', fontWeight: 800 }}>
            Step {step} of 2
          </div>
          <h2 style={{ fontSize: '20px', fontWeight: 800 }}>
            {step === 1 ? 'Describe Your Problem & Schedule' : 'Confirm & Review Summary'}
          </h2>
        </div>

        {step === 1 ? (
          <div>
            <div className="filter-group">
              <label>Service Category</label>
              <select
                className="filter-select"
                value={details.serviceId}
                onChange={e => setDetails({ ...details, serviceId: e.target.value })}
              >
                {services.map(s => <option key={s.id} value={s.id}>{s.name} (Base: ₹{s.basePrice})</option>)}
              </select>
            </div>

            <div className="filter-group">
              <label>Service Address</label>
              <input
                className="filter-input"
                value={details.address}
                onChange={e => setDetails({ ...details, address: e.target.value })}
              />
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '12px' }}>
              <div className="filter-group">
                <label>Date</label>
                <input
                  type="date"
                  className="filter-input"
                  value={details.date}
                  onChange={e => setDetails({ ...details, date: e.target.value })}
                />
              </div>
              <div className="filter-group">
                <label>Time Slot</label>
                <select
                  className="filter-select"
                  value={details.time}
                  onChange={e => setDetails({ ...details, time: e.target.value })}
                >
                  <option>10:00 AM - 12:00 PM</option>
                  <option>01:00 PM - 03:00 PM</option>
                  <option>04:00 PM - 06:00 PM</option>
                  <option>07:00 PM - 09:00 PM</option>
                </select>
              </div>
            </div>

            <div className="filter-group">
              <label>Instructions / Issue Description</label>
              <textarea
                className="filter-input"
                rows="3"
                placeholder="E.g. Main switchboard tripping frequently, please bring 32A MCB."
                value={details.description}
                onChange={e => setDetails({ ...details, description: e.target.value })}
              />
            </div>

            <button className="btn btn-primary btn-lg" style={{ width: '100%', marginTop: '10px' }} onClick={() => setStep(2)}>
              Proceed to Price Breakdown ➔
            </button>
          </div>
        ) : (
          <div>
            <div style={{ background: 'var(--light-bg)', border: '1px solid var(--border)', borderRadius: 'var(--radius)', padding: '16px', marginBottom: '16px' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '8px' }}>
                <span>Standard Inspection Fee</span>
                <b>₹{worker.rate || 499}</b>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '8px', color: 'var(--slate)', fontSize: '13px' }}>
                <span>WorkSaathi Shield & Safety Fee</span>
                <span>+ ₹49</span>
              </div>
              {discount > 0 && (
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '8px', color: 'var(--secondary)', fontSize: '13px', fontWeight: 700 }}>
                  <span>Promo Code Discount (FIRST100)</span>
                  <span>- ₹{discount}</span>
                </div>
              )}
              <div style={{ borderTop: '1px solid var(--border)', paddingTop: '10px', display: 'flex', justifyContent: 'space-between', fontSize: '18px', fontWeight: 800 }}>
                <span>Estimated Total</span>
                <span style={{ color: 'var(--primary)' }}>₹{finalAmount}</span>
              </div>
            </div>

            <div style={{ display: 'flex', gap: '8px', marginBottom: '16px' }}>
              <input
                className="filter-input"
                placeholder="Enter Promo Code (e.g. FIRST100)"
                value={coupon}
                onChange={e => setCoupon(e.target.value)}
              />
              <button className="btn btn-outline" onClick={applyCoupon}>Apply</button>
            </div>

            <div style={{ fontSize: '12px', color: 'var(--muted)', marginBottom: '18px' }}>
              💡 <b>Payment Mode:</b> Pay securely after the technician completes the service via UPI QR Code or Cash.
            </div>

            {message && <div style={{ color: 'var(--danger)', marginBottom: '12px', fontSize: '13px' }}>{message}</div>}

            <div style={{ display: 'flex', gap: '10px' }}>
              <button className="btn btn-outline" onClick={() => setStep(1)}>Back</button>
              <button className="btn btn-primary btn-lg" style={{ flex: 1 }} disabled={submitting} onClick={handleConfirm}>
                {submitting ? 'Submitting Booking...' : 'Confirm Booking ➔'}
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

/* =========================================
   LIVE CHAT MESSENGER MODAL
========================================= */
export function ChatModal({ job, auth, close }) {
  const [messages, setMessages] = useState([
    { id: 1, sender: 'system', text: `Chat connected with ${job.workerName || 'Specialist'}. Your contact numbers are masked for privacy.` },
    { id: 2, sender: 'other', text: `Hello ${auth?.name || 'Sir/Ma\'am'}, I have received your booking for ${job.title}. I am on the way.` }
  ]);
  const [text, setText] = useState('');

  const send = e => {
    if (e) e.preventDefault();
    if (!text.trim()) return;
    const userMsg = { id: Date.now(), sender: 'me', text: text.trim() };
    setMessages(prev => [...prev, userMsg]);
    setText('');

    // Simulated technician response
    setTimeout(() => {
      setMessages(prev => [...prev, { id: Date.now() + 1, sender: 'other', text: 'Understood! I will reach your gate in 10-15 minutes.' }]);
    }, 1500);
  };

  return (
    <div className="modal-overlay">
      <div className="modal-window" style={{ width: '480px' }}>
        <button className="close-btn" onClick={close}>✕</button>
        <h3 style={{ marginBottom: '16px', display: 'flex', alignItems: 'center', gap: '8px' }}>
          💬 Direct Chat: {job.workerName || 'Assigned Specialist'}
        </h3>

        <div className="chat-box-container">
          <div className="chat-messages-area">
            {messages.map(m => (
              <div key={m.id} className={`chat-bubble ${m.sender === 'me' ? 'sent' : m.sender === 'system' ? 'system' : 'received'}`}>
                {m.text}
              </div>
            ))}
          </div>

          <form className="chat-input-bar" onSubmit={send}>
            <input
              placeholder="Type message to technician..."
              value={text}
              onChange={e => setText(e.target.value)}
            />
            <button type="submit" className="btn btn-primary">Send</button>
          </form>
        </div>
      </div>
    </div>
  );
}

/* =========================================
   CUSTOMER DASHBOARD
========================================= */
export function CustomerDashboard({ setRole, setPage, setSelected, workerList = [], auth }) {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(false);
  const [activeChatJob, setActiveChatJob] = useState(null);

  const loadJobs = () => {
    setLoading(true);
    api.customerJobs()
      .then(data => setJobs(data || []))
      .catch(err => showToast(err.message, 'error'))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadJobs();
  }, []);

  const handleCancel = async (jobId) => {
    try {
      await api.cancelJob(jobId);
      showToast('Booking cancelled successfully', 'info');
      loadJobs();
    } catch (e) {
      showToast(e.message, 'error');
    }
  };

  const activeJobs = jobs.filter(j => ['REQUESTED', 'ACCEPTED', 'ON_THE_WAY', 'ARRIVED', 'IN_PROGRESS'].includes(j.status));
  const completedJobs = jobs.filter(j => j.status === 'COMPLETED');
  const totalSpent = completedJobs.reduce((acc, curr) => acc + (curr.finalPrice || curr.estimatedPrice || 0), 0);

  return (
    <main className="dashboard-page">
      <div className="dashboard-hero">
        <div>
          <div className="eyebrow-text">CUSTOMER CONTROL CENTER</div>
          <h1>Welcome back, {auth?.name || 'Customer'} 👋</h1>
          <p style={{ color: 'var(--muted)' }}>Manage live bookings, contact assigned pros, and view past history.</p>
        </div>
        <button className="btn btn-primary" onClick={() => setPage('find')}>+ Book New Service</button>
      </div>

      <div className="stats-grid">
        <div className="stat-card-modern">
          <strong>{activeJobs.length}</strong>
          <span>Active Bookings</span>
        </div>
        <div className="stat-card-modern">
          <strong>{completedJobs.length}</strong>
          <span>Completed Orders</span>
        </div>
        <div className="stat-card-modern">
          <strong>₹{totalSpent.toLocaleString()}</strong>
          <span>Total Spent</span>
        </div>
        <div className="stat-card-modern">
          <strong>4.9 ★</strong>
          <span>Customer Trust Score</span>
        </div>
      </div>

      <section style={{ marginBottom: '40px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '18px' }}>
          <h2>My Service Bookings ({jobs.length})</h2>
          <button className="btn btn-outline btn-sm" onClick={loadJobs}>Refresh ↻</button>
        </div>

        {loading ? (
          <div className="job-card-row" style={{ justifyContent: 'center' }}>Loading your bookings...</div>
        ) : jobs.length === 0 ? (
          <div className="job-card-row" style={{ flexDirection: 'column', textAlign: 'center', padding: '40px' }}>
            <h3>You have no bookings yet</h3>
            <p style={{ color: 'var(--muted)', margin: '8px 0 16px' }}>Need an electrician, plumber, or cleaning service?</p>
            <button className="btn btn-primary btn-sm" onClick={() => setPage('find')}>Explore Verified Pros ➔</button>
          </div>
        ) : (
          jobs.map(job => (
            <div className="job-card-row" key={job.id}>
              <div className="job-details-group">
                <div className="job-type-icon">{serviceIcons[job.serviceName] || '🔧'}</div>
                <div className="job-info-text">
                  <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                    <span className={`job-status-pill ${job.status}`}>● {job.status.replace(/_/g, ' ')}</span>
                    <small style={{ color: 'var(--muted)' }}>OTP: <b>{3410 + (job.id % 900)}</b></small>
                  </div>
                  <h3>{job.title} ({job.serviceName})</h3>
                  <p>{job.address} · Est: ₹{job.estimatedPrice || 500}</p>
                </div>
              </div>

              <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                <button className="btn btn-outline btn-sm" onClick={() => setActiveChatJob(job)}>
                  💬 Chat Pro
                </button>
                {['REQUESTED', 'ACCEPTED'].includes(job.status) && (
                  <button className="btn btn-danger-outline btn-sm" onClick={() => handleCancel(job.id)}>
                    Cancel
                  </button>
                )}
              </div>
            </div>
          ))
        )}
      </section>

      {activeChatJob && (
        <ChatModal job={activeChatJob} auth={auth} close={() => setActiveChatJob(null)} />
      )}
    </main>
  );
}

/* =========================================
   WORKER DASHBOARD
========================================= */
export function WorkerDashboard({ auth, setRole }) {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(false);
  const [isAvailable, setIsAvailable] = useState(true);

  const loadWorkerJobs = () => {
    setLoading(true);
    api.workerJobs()
      .then(data => setJobs(data || []))
      .catch(err => showToast(err.message, 'error'))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadWorkerJobs();
  }, []);

  const handleAction = async (actionFn, jobId, successMsg) => {
    try {
      await actionFn(jobId);
      showToast(successMsg || 'Job status updated', 'success');
      loadWorkerJobs();
    } catch (e) {
      showToast(e.message, 'error');
    }
  };

  const pendingRequests = jobs.filter(j => j.status === 'REQUESTED');
  const activeJobs = jobs.filter(j => ['ACCEPTED', 'ON_THE_WAY', 'ARRIVED', 'IN_PROGRESS'].includes(j.status));
  const completedJobs = jobs.filter(j => j.status === 'COMPLETED');
  const todayEarnings = completedJobs.reduce((acc, curr) => acc + (curr.finalPrice || curr.estimatedPrice || 0), 0);

  return (
    <main className="dashboard-page">
      <div className="dashboard-hero">
        <div>
          <div className="eyebrow-text">PARTNER WORKSPACE</div>
          <h1>Pro Dashboard: {auth?.name || 'Raj Kumar'} ⚡</h1>
          <p style={{ color: 'var(--muted)' }}>Manage incoming job requests, update travel status, and view weekly earnings.</p>
        </div>
        <button
          className={`btn ${isAvailable ? 'btn-secondary' : 'btn-outline'}`}
          onClick={() => {
            setIsAvailable(!isAvailable);
            showToast(`Status updated: ${!isAvailable ? 'Available for Jobs' : 'Offline'}`, 'info');
          }}
        >
          ● {isAvailable ? 'You are Available Online' : 'Currently Offline'}
        </button>
      </div>

      <div className="stats-grid">
        <div className="stat-card-modern">
          <strong>{pendingRequests.length}</strong>
          <span>New Job Requests</span>
        </div>
        <div className="stat-card-modern">
          <strong>{activeJobs.length}</strong>
          <span>Jobs In-Progress</span>
        </div>
        <div className="stat-card-modern">
          <strong>₹{todayEarnings.toLocaleString()}</strong>
          <span>Today's Earnings</span>
        </div>
        <div className="stat-card-modern">
          <strong>4.9 ★</strong>
          <span>Worker Rating</span>
        </div>
      </div>

      {/* NEW REQUESTS */}
      <section style={{ marginBottom: '36px' }}>
        <h2 style={{ marginBottom: '14px' }}>New Incoming Requests ({pendingRequests.length})</h2>
        {pendingRequests.length === 0 ? (
          <div className="job-card-row" style={{ justifyContent: 'center', padding: '30px' }}>
            <span style={{ color: 'var(--muted)' }}>No pending requests. You are ready to receive new bookings!</span>
          </div>
        ) : (
          pendingRequests.map(r => (
            <div className="job-card-row" key={r.id}>
              <div className="job-details-group">
                <div className="job-type-icon">{serviceIcons[r.serviceName] || '⚡'}</div>
                <div className="job-info-text">
                  <h3>{r.title} ({r.serviceName})</h3>
                  <p>Customer: <b>{r.customerName}</b> · {r.address}</p>
                  <small style={{ color: 'var(--muted)' }}>“{r.description}”</small>
                </div>
              </div>
              <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                <span style={{ fontSize: '18px', fontWeight: 800 }}>₹{r.estimatedPrice || 500}</span>
                <button className="btn btn-danger-outline btn-sm" onClick={() => handleAction(api.rejectJob, r.id, 'Job rejected')}>Decline</button>
                <button className="btn btn-primary btn-sm" onClick={() => handleAction(api.acceptJob, r.id, 'Job accepted! You can now start travel.')}>Accept Request</button>
              </div>
            </div>
          ))
        )}
      </section>

      {/* ACTIVE JOBS */}
      <section>
        <h2 style={{ marginBottom: '14px' }}>Active & In-Progress Jobs ({activeJobs.length})</h2>
        {activeJobs.length === 0 ? (
          <div className="job-card-row" style={{ justifyContent: 'center', padding: '30px' }}>
            <span style={{ color: 'var(--muted)' }}>No jobs currently in progress.</span>
          </div>
        ) : (
          activeJobs.map(job => (
            <div className="job-card-row" key={job.id}>
              <div className="job-details-group">
                <div className="job-type-icon">{serviceIcons[job.serviceName] || '🔧'}</div>
                <div className="job-info-text">
                  <span className={`job-status-pill ${job.status}`}>● {job.status.replace(/_/g, ' ')}</span>
                  <h3>{job.title} · Customer: {job.customerName}</h3>
                  <p>{job.address}</p>
                </div>
              </div>
              <div style={{ display: 'flex', gap: '8px' }}>
                {job.status === 'ACCEPTED' && (
                  <button className="btn btn-primary" onClick={() => handleAction(api.onTheWay, job.id, 'Status set to On The Way 🚗')}>
                    Mark On The Way 🚗
                  </button>
                )}
                {job.status === 'ON_THE_WAY' && (
                  <button className="btn btn-primary" onClick={() => handleAction(api.arrived, job.id, 'Status set to Arrived 📍')}>
                    Mark Arrived 📍
                  </button>
                )}
                {job.status === 'ARRIVED' && (
                  <button className="btn btn-primary" onClick={() => handleAction(api.startJob, job.id, 'Job Started ⚙️')}>
                    Start Work ⚙️
                  </button>
                )}
                {job.status === 'IN_PROGRESS' && (
                  <button className="btn btn-secondary" onClick={() => handleAction(api.completeJob, job.id, 'Job Completed! ✅')}>
                    Complete Job ✅
                  </button>
                )}
              </div>
            </div>
          ))
        )}
      </section>
    </main>
  );
}

/* =========================================
   ADMIN DASHBOARD
========================================= */
export function AdminDashboard({ auth }) {
  const [stats, setStats] = useState({ totalUsers: 10, totalWorkers: 5, verifiedWorkers: 4, pendingVerifications: 1 });
  const [pendingWorkers, setPendingWorkers] = useState([]);
  const [loading, setLoading] = useState(false);

  const loadAdminData = () => {
    setLoading(true);
    Promise.all([
      api.adminDashboard().catch(() => stats),
      api.adminPendingWorkers().catch(() => [])
    ]).then(([dashboardStats, pending]) => {
      if (dashboardStats) setStats(dashboardStats);
      if (pending) setPendingWorkers(pending);
    }).finally(() => setLoading(false));
  };

  useEffect(() => {
    loadAdminData();
  }, []);

  const handleVerify = async (workerId) => {
    try {
      await api.verifyWorker(workerId);
      showToast('Worker profile verified & approved!', 'success');
      loadAdminData();
    } catch (e) {
      showToast(e.message, 'error');
    }
  };

  return (
    <main className="dashboard-page">
      <div className="dashboard-hero">
        <div>
          <div className="eyebrow-text">ADMINISTRATION PLATFORM</div>
          <h1>System Overview & Compliance Control</h1>
          <p style={{ color: 'var(--muted)' }}>Real-time ecosystem statistics, identity verification queue, and safety auditing.</p>
        </div>
        <button className="btn btn-outline" onClick={loadAdminData}>Refresh Stats ↻</button>
      </div>

      <div className="stats-grid">
        <div className="stat-card-modern">
          <strong>{stats.totalUsers || 12}</strong>
          <span>Total Registered Users</span>
        </div>
        <div className="stat-card-modern">
          <strong>{stats.totalWorkers || 6}</strong>
          <span>Registered Pros</span>
        </div>
        <div className="stat-card-modern">
          <strong>{stats.verifiedWorkers || 5}</strong>
          <span>Verified Specialists</span>
        </div>
        <div className="stat-card-modern">
          <strong style={{ color: pendingWorkers.length > 0 ? 'var(--accent)' : 'var(--dark)' }}>
            {pendingWorkers.length}
          </strong>
          <span>Pending Verifications</span>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1.2fr 0.8fr', gap: '24px' }}>
        <div style={{ background: '#fff', border: '1px solid var(--border)', borderRadius: 'var(--radius)', padding: '24px' }}>
          <h3 style={{ marginBottom: '6px' }}>Pro Verification Queue ({pendingWorkers.length} pending)</h3>
          <p style={{ fontSize: '13px', color: 'var(--muted)', marginBottom: '18px' }}>
            Review government ID credentials and trade certificates before granting badge.
          </p>

          {pendingWorkers.length === 0 ? (
            <div style={{ padding: '30px', textAlign: 'center', background: 'var(--light-bg)', borderRadius: 'var(--radius-sm)' }}>
              <b>All registered professionals are currently verified!</b>
            </div>
          ) : (
            pendingWorkers.map(w => (
              <div key={w.id} style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '12px 0', borderBottom: '1px solid var(--border)' }}>
                <div style={{ display: 'flex', gap: '12px', alignItems: 'center' }}>
                  <div className="worker-avatar" style={{ width: '40px', height: '40px', fontSize: '14px', background: '#0f62fe' }}>
                    {w.name?.slice(0, 2).toUpperCase()}
                  </div>
                  <div>
                    <b>{w.name}</b>
                    <div style={{ fontSize: '12px', color: 'var(--muted)' }}>{w.bio || 'New Technician Registration'}</div>
                  </div>
                </div>
                <button className="btn btn-secondary btn-sm" onClick={() => handleVerify(w.id)}>
                  Approve Pro ✓
                </button>
              </div>
            ))
          )}
        </div>

        <div style={{ background: '#fff', border: '1px solid var(--border)', borderRadius: 'var(--radius)', padding: '24px' }}>
          <h3 style={{ marginBottom: '6px' }}>Weekly Completed Service Volume</h3>
          <p style={{ fontSize: '13px', color: 'var(--muted)', marginBottom: '18px' }}>
            Bookings up <b style={{ color: 'var(--secondary)' }}>+24.8%</b> this month across Delhi-NCR.
          </p>
          <div style={{ height: '140px', display: 'flex', alignItems: 'flex-end', gap: '12px', borderBottom: '1px solid var(--border)', paddingBottom: '10px' }}>
            {[40, 65, 55, 80, 95, 85, 90].map((h, i) => (
              <div key={i} style={{ flex: 1, display: 'flex', flexDirection: 'column', alignItems: 'center', gap: '6px' }}>
                <div style={{ width: '100%', height: `${h}%`, background: 'var(--primary)', borderRadius: '4px 4px 0 0' }}></div>
                <span style={{ fontSize: '11px', color: 'var(--muted)' }}>{['M', 'T', 'W', 'T', 'F', 'S', 'S'][i]}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </main>
  );
}

/* =========================================
   AUTHENTICATION PAGE
========================================= */
export function AuthPage({ onAuthenticated }) {
  const [mode, setMode] = useState('login');
  const [form, setForm] = useState({ name: '', email: '', phone: '', password: '', role: 'CUSTOMER' });
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);

  const submit = async e => {
    if (e) e.preventDefault();
    setBusy(true);
    setError('');
    try {
      const result = mode === 'login'
        ? await api.login(form.email, form.password)
        : await api.register(form);

      localStorage.setItem('worksaathi_access_token', result.accessToken);
      localStorage.setItem('worksaathi_refresh_token', result.refreshToken);
      showToast('Login successful! Welcome to WorkSaathi.', 'success');
      onAuthenticated(result);
    } catch (err) {
      setError(err.message || 'Authentication failed');
    } finally {
      setBusy(false);
    }
  };

  const fillDemo = (email, role) => {
    setMode('login');
    setForm({ ...form, email, password: 'password123' });
    showToast(`Autofilled demo ${role} credentials`, 'info');
  };

  return (
    <main className="auth-container">
      <div className="auth-card-elevated">
        <div className="brand" style={{ marginBottom: '16px' }}>
          <div className="brand-icon">W</div>
          <div>Work<span>Saathi</span></div>
        </div>

        <h2 style={{ fontSize: '24px', fontWeight: 800, marginBottom: '6px' }}>
          {mode === 'login' ? 'Welcome Back' : 'Create Your Account'}
        </h2>
        <p style={{ fontSize: '13px', color: 'var(--muted)', marginBottom: '20px' }}>
          {mode === 'login' ? 'Log in to book services or manage your pro workspace.' : 'Sign up to find certified pros or grow your service business.'}
        </p>

        <form onSubmit={submit}>
          {mode === 'register' && (
            <>
              <div className="filter-group">
                <label>
                  Full Name
                  <input
                    required
                    className="filter-input"
                    value={form.name}
                    onChange={e => setForm({ ...form, name: e.target.value })}
                  />
                </label>
              </div>
              <div className="filter-group">
                <label>
                  Phone Number
                  <input
                    required
                    className="filter-input"
                    value={form.phone}
                    onChange={e => setForm({ ...form, phone: e.target.value })}
                  />
                </label>
              </div>
            </>
          )}

          <div className="filter-group">
            <label>
              Email Address
              <input
                required
                type="email"
                className="filter-input"
                value={form.email}
                onChange={e => setForm({ ...form, email: e.target.value })}
              />
            </label>
          </div>

          <div className="filter-group">
            <label>
              Password
              <input
                required
                type="password"
                className="filter-input"
                value={form.password}
                onChange={e => setForm({ ...form, password: e.target.value })}
              />
            </label>
          </div>

          {mode === 'register' && (
            <div className="filter-group">
              <label>
                Join as
                <select
                  className="filter-select"
                  value={form.role}
                  onChange={e => setForm({ ...form, role: e.target.value })}
                >
                  <option value="CUSTOMER">Customer (Book Services)</option>
                  <option value="WORKER">Worker Specialist (Provide Services)</option>
                </select>
              </label>
            </div>
          )}

          {error && <div style={{ color: 'var(--danger)', marginBottom: '14px', fontSize: '13px' }}>{error}</div>}

          <button type="submit" className="btn btn-primary btn-lg" style={{ width: '100%', marginTop: '6px' }} disabled={busy}>
            {busy ? 'Verifying...' : mode === 'login' ? 'Sign In ➔' : 'Create Account ➔'}
          </button>
        </form>

        <div style={{ textAlign: 'center', marginTop: '16px' }}>
          <button
            type="button"
            style={{ fontSize: '13px', fontWeight: 600, color: 'var(--primary)' }}
            onClick={() => setMode(mode === 'login' ? 'register' : 'login')}
          >
            {mode === 'login' ? "Don't have an account? Sign up" : 'Already have an account? Sign in'}
          </button>
        </div>

        <div style={{ marginTop: '24px', paddingTop: '16px', borderTop: '1px solid var(--border)' }}>
          <small style={{ fontSize: '11px', fontWeight: 700, color: 'var(--muted)', textTransform: 'uppercase' }}>1-Click Instant Demo Login</small>
          <div className="demo-pills-row">
            <button type="button" className="demo-pill-btn" onClick={() => fillDemo('customer@worksaathi.com', 'Customer')}>
              👨 Customer
            </button>
            <button type="button" className="demo-pill-btn" onClick={() => fillDemo('raj@worksaathi.com', 'Worker')}>
              ⚡ Electrician
            </button>
            <button type="button" className="demo-pill-btn" onClick={() => fillDemo('admin@worksaathi.com', 'Admin')}>
              🛡️ Admin
            </button>
          </div>
        </div>
      </div>
    </main>
  );
}

/* =========================================
   FOOTER
========================================= */
export function Footer() {
  return (
    <footer style={{ background: '#0f172a', color: '#fff', padding: '60px max(4vw, 24px) 30px', borderTop: '1px solid #1e293b' }}>
      <div style={{ maxWidth: '1280px', margin: '0 auto', display: 'grid', gridTemplateColumns: '1.5fr 1fr 1fr 1fr', gap: '40px', marginBottom: '40px' }}>
        <div>
          <div className="brand" style={{ color: '#fff', marginBottom: '14px' }}>
            <div className="brand-icon">W</div>
            <div>Work<span style={{ color: '#34d399' }}>Saathi</span></div>
          </div>
          <p style={{ color: '#94a3b8', fontSize: '14px', maxWidth: '320px', lineHeight: 1.6 }}>
            India's premier technology platform connecting homeowners and commercial enterprises with certified, background-checked service professionals.
          </p>
        </div>
        <div>
          <b style={{ display: 'block', marginBottom: '14px', fontSize: '14px' }}>Popular Services</b>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', fontSize: '13px', color: '#94a3b8' }}>
            <span>⚡ Electrical Repairs</span>
            <span>🔧 Plumbing & Leaks</span>
            <span>❄️ AC Jet Cleaning</span>
            <span>✨ Deep Sanitization</span>
          </div>
        </div>
        <div>
          <b style={{ display: 'block', marginBottom: '14px', fontSize: '14px' }}>Cities Covered</b>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', fontSize: '13px', color: '#94a3b8' }}>
            <span>Delhi NCR (Gurgaon/Noida)</span>
            <span>Mumbai & Navi Mumbai</span>
            <span>Bengaluru (Central & East)</span>
            <span>Hyderabad & Pune</span>
          </div>
        </div>
        <div>
          <b style={{ display: 'block', marginBottom: '14px', fontSize: '14px' }}>Trust & Safety</b>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px', fontSize: '13px', color: '#94a3b8' }}>
            <span>🛡️ ₹10,000 Cover Guarantee</span>
            <span>🔍 7-Point Background Check</span>
            <span>💳 Escrow Post-Service Pay</span>
            <span>📞 24/7 Priority Helpline</span>
          </div>
        </div>
      </div>
      <div style={{ maxWidth: '1280px', margin: '0 auto', borderTop: '1px solid #1e293b', paddingTop: '20px', display: 'flex', justifyContent: 'space-between', fontSize: '12px', color: '#64748b' }}>
        <span>© 2026 WorkSaathi Technologies India Pvt. Ltd. All rights reserved.</span>
        <span>Made with ❤️ for Indian Homes</span>
      </div>
    </footer>
  );
}

export function mapWorker(worker, index = 0) {
  const palette = ['#0f62fe', '#00a676', '#f59e0b', '#9333ea'];
  const names = (worker?.name || 'Local Specialist').split(' ');
  const initials = names.map(n => n[0]).join('').slice(0, 2).toUpperCase();

  return {
    id: worker.id,
    name: worker.name || 'Local Specialist',
    role: worker.services?.[0] || 'Verified Specialist',
    rating: worker.averageRating || 4.8,
    reviews: worker.totalReviews || 18,
    rate: worker.dailyRate || worker.hourlyRate || 499,
    distance: worker.distance || (1.2 + (index * 0.5)),
    exp: worker.experienceYears || 5,
    bio: worker.bio,
    photo: initials,
    color: palette[index % palette.length],
    available: worker.availabilityStatus === 'AVAILABLE' || worker.availabilityStatus === 'APPROVED',
    skills: worker.services?.length ? worker.services : ['General Maintenance', 'Inspection', 'Doorstep Repair']
  };
}

/* =========================================
   MAIN APP ORCHESTRATOR
========================================= */
export function App() {
  const [page, setPage] = useState('home');
  const [role, setRole] = useState('customer');
  const [query, setQuery] = useState('');
  const [city, setCity] = useState('New Delhi (NCR)');
  const [selected, setSelected] = useState(fallbackWorkers[0]);
  const [booking, setBooking] = useState(false);
  const [auth, setAuth] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem('worksaathi_user'));
    } catch {
      return null;
    }
  });

  const [liveWorkers, setLiveWorkers] = useState([]);
  const [servicesList, setServicesList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [apiError, setApiError] = useState('');

  const refreshData = () => {
    setLoading(true);
    Promise.all([
      api.workers().catch(() => ({ content: [] })),
      api.services().catch(() => fallbackServices)
    ]).then(([workerPage, serviceData]) => {
      const mapped = (workerPage.content || []).map(mapWorker);
      if (mapped.length > 0) setLiveWorkers(mapped);
      setServicesList(serviceData || fallbackServices);
    }).catch(e => setApiError(e.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    refreshData();
  }, []);

  const authenticated = result => {
    const user = { name: result.name, role: result.role, email: result.email };
    localStorage.setItem('worksaathi_user', JSON.stringify(user));
    setAuth(user);
    const lowerRole = (result.role || 'customer').toLowerCase();
    setRole(lowerRole);
    setPage('dashboard');
  };

  const logout = () => {
    localStorage.removeItem('worksaathi_access_token');
    localStorage.removeItem('worksaathi_refresh_token');
    localStorage.removeItem('worksaathi_user');
    setAuth(null);
    setRole('customer');
    setPage('home');
    showToast('Logged out successfully', 'info');
  };

  const workerList = liveWorkers.length ? liveWorkers : fallbackWorkers;

  return (
    <>
      <ToastContainer />

      <Header
        page={page}
        setPage={setPage}
        role={role}
        setRole={setRole}
        auth={auth}
        onLogout={logout}
        city={city}
        setCity={setCity}
      />

      {page === 'home' && (
        <Home setPage={setPage} setQuery={setQuery} servicesList={servicesList} />
      )}

      {page === 'auth' && (
        <AuthPage onAuthenticated={authenticated} />
      )}

      {page === 'find' && (
        <FindWorkers
          query={query}
          setQuery={setQuery}
          setPage={setPage}
          setSelected={setSelected}
          workerList={workerList}
          servicesList={servicesList}
          loading={loading}
          error={apiError}
          city={city}
        />
      )}

      {page === 'profile' && (
        <Profile worker={selected} setPage={setPage} setBooking={setBooking} />
      )}

      {page === 'dashboard' && (
        role === 'worker' ? (
          <WorkerDashboard auth={auth} setRole={setRole} />
        ) : role === 'admin' ? (
          <AdminDashboard auth={auth} />
        ) : (
          <CustomerDashboard
            setRole={setRole}
            setPage={setPage}
            setSelected={setSelected}
            workerList={workerList}
            auth={auth}
          />
        )
      )}

      {booking && (
        <Booking
          worker={selected}
          close={() => setBooking(false)}
          setPage={setPage}
          services={servicesList}
          auth={auth}
          onJobCreated={refreshData}
        />
      )}
    </>
  );
}

const rootEl = document.getElementById('root');
if (rootEl) {
  createRoot(rootEl).render(<App />);
}
