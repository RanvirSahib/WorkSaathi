import React, { useEffect, useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { api } from './services/api';
import './styles.css';

export const fallbackWorkers = [
  { id: 1, name: 'Raj Kumar', role: 'Electrician', rating: 4.8, reviews: 127, rate: 500, distance: 2.4, exp: 5, photo: 'RK', color: '#195a9b', available: true, skills: ['Electrician', 'AC Service'] },
  { id: 2, name: 'Sanjay Verma', role: 'Plumber', rating: 4.9, reviews: 92, rate: 450, distance: 1.8, exp: 7, photo: 'SV', color: '#0b8a79', available: true, skills: ['Plumber'] },
  { id: 3, name: 'Amit Singh', role: 'Carpenter', rating: 4.7, reviews: 84, rate: 600, distance: 3.1, exp: 6, photo: 'AS', color: '#a15c26', available: true, skills: ['Carpenter'] },
  { id: 4, name: 'Priya Sharma', role: 'Home Cleaning', rating: 4.9, reviews: 204, rate: 350, distance: 2.7, exp: 4, photo: 'PS', color: '#944e78', available: true, skills: ['Home Cleaning'] }
];

export const fallbackServices = [
  { id: 1, name: 'Electrician', description: 'Wiring, repairs & installations', category: 'Electrical', basePrice: 150 },
  { id: 2, name: 'Plumber', description: 'Leak repairs & pipe fittings', category: 'Plumbing', basePrice: 120 },
  { id: 3, name: 'Carpenter', description: 'Furniture making & woodwork', category: 'Carpentry', basePrice: 200 },
  { id: 4, name: 'Home Cleaning', description: 'Deep home & office cleaning', category: 'Cleaning', basePrice: 300 },
  { id: 5, name: 'AC Service', description: 'AC repair & maintenance', category: 'Appliances', basePrice: 250 },
  { id: 6, name: 'Painter', description: 'Interior & exterior painting', category: 'Painting', basePrice: 350 },
  { id: 7, name: 'Mechanic', description: 'Two-wheeler & four-wheeler servicing', category: 'Automotive', basePrice: 200 },
  { id: 8, name: 'Gardening', description: 'Lawn care & plant maintenance', category: 'Outdoor', basePrice: 100 }
];

export const serviceIcons = {
  'Electrician': '⚡',
  'Plumber': '🔧',
  'Carpenter': '🪚',
  'Home Cleaning': '✨',
  'AC Service': '❄️',
  'Painter': '🎨',
  'Mechanic': '🚗',
  'Gardening': '🌱'
};

export function Avatar({ worker, large = false }) {
  return <div className={'avatar ' + (large ? 'large' : '')} style={{ background: worker?.color || '#195a9b' }}>{worker?.photo || 'WS'}</div>;
}

export function Stars({ value }) {
  return <span className="stars">★ <b>{Number(value || 0).toFixed(1)}</b></span>;
}

export function Header({ page, setPage, role, setRole, auth, onLogout }) {
  return (
    <header>
      <button className="brand" onClick={() => setPage('home')}>
        <i>W</i> Work<span>Saathi</span>
      </button>
      <nav>
        <button onClick={() => setPage('find')}>Find workers</button>
        <button onClick={() => setPage('how')}>How it works</button>
        <button onClick={() => setPage('about')}>About</button>
      </nav>
      <div className="header-actions">
        {auth ? (
          <>
            <button className="login" onClick={() => setPage('dashboard')}>
              👤 {auth.name} ({auth.role})
            </button>
            <button className="join" onClick={onLogout}>Log out</button>
          </>
        ) : (
          <>
            <button className="login" onClick={() => setPage('auth')}>Log in</button>
            <button className="join" onClick={() => setPage('auth')}>Join WorkSaathi <span>→</span></button>
          </>
        )}
        <select
          aria-label="View role"
          value={role}
          onChange={e => {
            const nextRole = e.target.value;
            setRole(nextRole);
            setPage('dashboard');
          }}
        >
          <option value="customer">Customer view</option>
          <option value="worker">Worker view</option>
          <option value="admin">Admin view</option>
        </select>
      </div>
    </header>
  );
}

export function Home({ setPage, setQuery, servicesList = [] }) {
  const displayServices = servicesList.length ? servicesList : fallbackServices;

  return (
    <>
      <section className="hero">
        <div className="hero-copy">
          <div className="eyebrow">✦ INDIA'S TRUSTED LOCAL SERVICES PLATFORM</div>
          <h1>The right person<br />for <em>every</em> job.</h1>
          <p>Book skilled, verified local professionals for the things that matter at home.</p>
          <div className="search-bar">
            <span>⌕</span>
            <input
              aria-label="Search a service"
              placeholder="What service do you need?"
              onChange={e => setQuery(e.target.value)}
              onKeyDown={e => e.key === 'Enter' && setPage('find')}
            />
            <button onClick={() => setPage('find')}>Find a pro <b>→</b></button>
          </div>
          <div className="popular">
            <span>Popular:</span>
            {['Electrician', 'Plumber', 'Home Cleaning', 'AC Service'].map(x => (
              <button key={x} onClick={() => { setQuery(x); setPage('find'); }}>{x}</button>
            ))}
          </div>
        </div>
        <div className="hero-visual">
          <div className="halo"></div>
          <div className="house">
            <div className="roof"></div>
            <div className="wall">
              <span className="window"></span>
              <span className="door"></span>
            </div>
          </div>
          <div className="float-card verified">✓ <span><b>Every pro verified</b><small>Background checked</small></span></div>
          <div className="float-card rating">
            <span className="rating-stars">★★★★★</span>
            <b>4.8 average rating</b>
            <small>From 10,000+ reviews</small>
          </div>
          <div className="float-card location">● <span><b>Available near you</b><small>2.4 km away</small></span></div>
        </div>
      </section>

      <section className="trust-strip">
        <div><b>10,000+</b><span>Verified professionals</span></div>
        <div><b>50,000+</b><span>Jobs completed</span></div>
        <div><b>4.8 / 5</b><span>Average customer rating</span></div>
        <div><b>25+ cities</b><span>And growing every month</span></div>
      </section>

      <section className="services section">
        <div className="section-heading">
          <div>
            <div className="eyebrow">SERVICES THAT MAKE LIFE EASIER</div>
            <h2>Whatever needs doing,<br /><em>we know someone.</em></h2>
          </div>
          <button className="text-button" onClick={() => setPage('find')}>Explore all services <b>→</b></button>
        </div>
        <div className="service-grid">
          {displayServices.map(s => {
            const icon = serviceIcons[s.name] || '🛠️';
            return (
              <button
                className="service-card"
                key={s.id || s.name}
                onClick={() => { setQuery(s.name); setPage('find'); }}
              >
                <span className="service-icon">{icon}</span>
                <b>{s.name}</b>
                <small>{s.description || 'Professional on-demand service'}</small>
                <i>→</i>
              </button>
            );
          })}
        </div>
      </section>

      <section className="how section">
        <div className="eyebrow">SIMPLE FROM START TO FINISH</div>
        <h2>Getting help has never<br />been <em>this easy.</em></h2>
        <div className="steps">
          <div>
            <span>01</span>
            <b>Tell us what you need</b>
            <p>Search by service, location, or the task you have in mind.</p>
          </div>
          <div>
            <span>02</span>
            <b>Choose your professional</b>
            <p>Compare profiles, ratings, prices, and availability.</p>
          </div>
          <div>
            <span>03</span>
            <b>Book, relax, done</b>
            <p>Pick a time that works. Track your job from start to finish.</p>
          </div>
        </div>
      </section>

      <section className="cta">
        <div>
          <div className="eyebrow">FOR SKILLED PROFESSIONALS</div>
          <h2>Great work deserves<br /><em>great opportunities.</em></h2>
          <p>Join thousands of trusted professionals growing their businesses with WorkSaathi.</p>
          <button className="light-button" onClick={() => setPage('auth')}>Become a WorkSaathi pro <b>→</b></button>
        </div>
        <div className="cta-art">✦<span>Earn on your terms</span><span>Build your reputation</span></div>
      </section>
      <Footer />
    </>
  );
}

export function FindWorkers({ query, setQuery, setPage, setSelected, workerList = [], servicesList = [], loading, error }) {
  const [onlyAvailable, setOnlyAvailable] = useState(false);
  const [selectedService, setSelectedService] = useState('All');
  const [sort, setSort] = useState('Recommended');

  const result = useMemo(() => {
    return workerList.filter(w => {
      const matchAvail = !onlyAvailable || w.available;
      const matchService = selectedService === 'All' || (w.skills && w.skills.includes(selectedService)) || w.role === selectedService;
      const matchQuery = !query || `${w.role} ${w.name} ${(w.skills || []).join(' ')}`.toLowerCase().includes(query.toLowerCase());
      return matchAvail && matchService && matchQuery;
    }).sort((a, b) => {
      if (sort === 'Rating') return b.rating - a.rating;
      if (sort === 'Price') return a.rate - b.rate;
      return a.distance - b.distance;
    });
  }, [query, onlyAvailable, selectedService, sort, workerList]);

  return (
    <main className="finder section">
      <div className="breadcrumb">Home / Find workers</div>
      <h1>Find trusted professionals<br /><em>near you.</em></h1>

      <div className="finder-search">
        <span>⌕</span>
        <input
          value={query}
          onChange={e => setQuery(e.target.value)}
          placeholder="Search by worker name, trade, or skill..."
        />
        <button onClick={() => {}}>Search</button>
      </div>

      <div className="finder-layout">
        <aside>
          <div className="filter-head">
            <b>Filters</b>
            <button onClick={() => { setQuery(''); setSelectedService('All'); setOnlyAvailable(false); }}>Reset all</button>
          </div>
          <label>
            Service
            <select value={selectedService} onChange={e => setSelectedService(e.target.value)}>
              <option value="All">All services</option>
              {servicesList.map(s => <option key={s.id} value={s.name}>{s.name}</option>)}
            </select>
          </label>
          <label>
            Location
            <select>
              <option>New Delhi (Default)</option>
              <option>Within 5 km</option>
              <option>Within 15 km</option>
            </select>
          </label>
          <label>
            Price range
            <div className="price">₹100 <span>—</span> ₹1,500/day</div>
          </label>
          <label className="check">
            <input type="checkbox" checked={onlyAvailable} onChange={e => setOnlyAvailable(e.target.checked)} />
            Available today
          </label>
          <label className="check">
            <input type="checkbox" defaultChecked readOnly />
            Verified professionals only
          </label>
        </aside>

        <div className="worker-results">
          <div className="result-head">
            <span><b>{result.length} professionals</b> near New Delhi</span>
            <label>
              Sort:
              <select value={sort} onChange={e => setSort(e.target.value)}>
                <option>Recommended</option>
                <option>Rating</option>
                <option>Price</option>
              </select>
            </label>
          </div>

          {loading ? (
            <div className="empty"><strong>Finding local professionals…</strong></div>
          ) : error ? (
            <div className="empty"><strong>Couldn’t load live workers</strong><span>{error}</span></div>
          ) : result.length ? (
            result.map(w => (
              <WorkerCard key={w.id} worker={w} onView={() => { setSelected(w); setPage('profile'); }} />
            ))
          ) : (
            <div className="empty">
              <strong>No workers found</strong>
              <span>Try adjusting your search filters or selected category.</span>
            </div>
          )}
        </div>
      </div>
    </main>
  );
}

export function WorkerCard({ worker, onView }) {
  return (
    <article className="worker-card">
      <Avatar worker={worker} />
      <div className="worker-info">
        <h3>{worker.name} <span className="verified-mini">✓</span></h3>
        <p>{worker.role} · {worker.exp || 3}+ years exp.</p>
        <div><Stars value={worker.rating} /> <span className="muted">({worker.reviews} reviews)</span></div>
        <div className="tags">
          {(worker.skills || []).slice(0, 3).map(x => <span key={x}>{x}</span>)}
        </div>
      </div>
      <div className="worker-meta">
        <span className={worker.available ? 'availability' : 'unavailable'}>
          ● {worker.available ? 'Available today' : 'Currently booked'}
        </span>
        <b>₹{worker.rate}<small>/ day</small></b>
        <span className="muted">⌖ {worker.distance ? `${worker.distance.toFixed(1)} km away` : 'Near you'}</span>
        <button className="outline-button" onClick={onView}>View profile</button>
      </div>
    </article>
  );
}

export function Profile({ worker, setPage, setBooking }) {
  return (
    <main className="profile section">
      <button className="back" onClick={() => setPage('find')}>← Back to results</button>
      <div className="profile-grid">
        <div>
          <section className="profile-hero">
            <Avatar worker={worker} large />
            <div className="profile-top">
              <div>
                <h1>{worker.name} <span className="verified-mini">✓</span></h1>
                <p>{worker.role} · New Delhi · {worker.exp || 3}+ years experience</p>
                <Stars value={worker.rating} /> <span className="muted">{worker.reviews} reviews</span>
              </div>
              <span className={worker.available ? 'availability' : 'unavailable'}>
                ● {worker.available ? 'Available today' : 'Unavailable'}
              </span>
            </div>
          </section>

          <section className="profile-section">
            <h3>About {worker.name.split(' ')[0]}</h3>
            <p>{worker.bio || `Reliable and detail-oriented ${worker.role.toLowerCase()} with ${worker.exp || 3}+ years of hands-on experience in residential and commercial maintenance. Known for clear communication, tidy work, and punctuality.`}</p>
          </section>

          <section className="profile-section">
            <h3>Skills & services</h3>
            <div className="tags big">
              {(worker.skills || []).map(x => <span key={x}>{x}</span>)}
            </div>
          </section>

          <section className="profile-section">
            <div className="review-title">
              <h3>Verified customer reviews</h3>
              <button>See all reviews →</button>
            </div>
            <blockquote>
              “Very professional and punctual. Explained the issue clearly and fixed it quickly. Highly recommend!”
              <footer>— Ankit M., verified customer</footer>
            </blockquote>
          </section>
        </div>

        <aside className="booking-card">
          <span className="eyebrow">STARTING FROM</span>
          <strong>₹{worker.rate}<small> / day</small></strong>
          <p>Final price is agreed with your professional before work begins.</p>
          <button className="primary wide" onClick={() => setBooking(true)}>Request service <b>→</b></button>
          <div className="booking-checks">
            <span>✓ No booking fees</span>
            <span>✓ Pay after completion</span>
            <span>✓ Verified professional guarantee</span>
          </div>
        </aside>
      </div>
    </main>
  );
}

export function Booking({ worker, close, setPage, services = [], auth, onJobCreated }) {
  const [step, setStep] = useState(1);
  const [message, setMessage] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const matchedService = services.find(s => (worker.skills || []).includes(s.name)) || services[0];
  const [details, setDetails] = useState({
    serviceId: matchedService?.id || (services[0]?.id || 1),
    description: '',
    address: 'Flat 402, Green Valley Apartments, New Delhi',
    date: new Date(Date.now() + 86400000).toISOString().split('T')[0],
    time: '10:00 AM'
  });

  const next = async () => {
    if (step < 3) return setStep(step + 1);
    if (!auth) {
      setMessage('Please log in before sending a service request.');
      return;
    }
    setSubmitting(true);
    setMessage('');
    try {
      await api.createJob({
        workerId: worker.id,
        serviceId: Number(details.serviceId),
        title: `${worker.role} request`,
        description: details.description || 'On-demand service booking via WorkSaathi web app',
        scheduledDate: `${details.date}T10:00:00`,
        scheduledTime: details.time,
        address: details.address,
        estimatedPrice: worker.rate
      });
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
    <div className="modal-backdrop">
      <section className="modal">
        <button className="modal-close" onClick={close}>×</button>
        <div className="progress">
          <span className={step >= 1 ? 'active' : ''}>1<br /><small>Details</small></span>
          <i></i>
          <span className={step >= 2 ? 'active' : ''}>2<br /><small>Schedule</small></span>
          <i></i>
          <span className={step >= 3 ? 'active' : ''}>3<br /><small>Confirm</small></span>
        </div>

        {step === 1 && (
          <>
            <h2>Tell {worker.name.split(' ')[0]} what you need</h2>
            <label>
              Service
              <select value={details.serviceId} onChange={e => setDetails({ ...details, serviceId: e.target.value })}>
                {services.map(s => <option key={s.id} value={s.id}>{s.name} (Base: ₹{s.basePrice})</option>)}
              </select>
            </label>
            <label>
              Describe the problem
              <textarea
                value={details.description}
                onChange={e => setDetails({ ...details, description: e.target.value })}
                placeholder="For example: AC is not cooling properly and making a buzzing noise."
              />
            </label>
          </>
        )}

        {step === 2 && (
          <>
            <h2>When should they visit?</h2>
            <label>
              Service Address
              <input value={details.address} onChange={e => setDetails({ ...details, address: e.target.value })} />
            </label>
            <div className="two-inputs">
              <label>
                Date
                <input type="date" value={details.date} onChange={e => setDetails({ ...details, date: e.target.value })} />
              </label>
              <label>
                Time Slot
                <select value={details.time} onChange={e => setDetails({ ...details, time: e.target.value })}>
                  <option>10:00 AM</option>
                  <option>01:00 PM</option>
                  <option>04:00 PM</option>
                  <option>07:00 PM</option>
                </select>
              </label>
            </div>
          </>
        )}

        {step === 3 && (
          <>
            <h2>Ready to submit request?</h2>
            <div className="request-summary">
              <Avatar worker={worker} />
              <div>
                <b>{worker.name}</b>
                <span>{worker.role}</span>
                <span>{details.date} · {details.time}</span>
              </div>
              <strong>₹{worker.rate}<small> est.</small></strong>
            </div>
            <p className="muted" style={{ marginTop: '12px' }}>
              The professional will review your request and confirm availability directly.
            </p>
          </>
        )}

        {message && <p className="form-error">{message}</p>}
        <button className="primary wide" disabled={submitting} onClick={next}>
          {submitting ? 'Submitting…' : step === 3 ? 'Confirm & Send Request' : 'Continue'} <b>→</b>
        </button>
      </section>
    </div>
  );
}

export function CustomerDashboard({ setRole, setPage, setSelected, workerList = [], auth }) {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState('');

  const loadJobs = () => {
    setLoading(true);
    api.customerJobs()
      .then(data => setJobs(data || []))
      .catch(err => setMsg(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadJobs();
  }, []);

  const handleCancel = async (jobId) => {
    try {
      await api.cancelJob(jobId);
      loadJobs();
    } catch (e) {
      alert(e.message);
    }
  };

  const activeJobs = jobs.filter(j => ['REQUESTED', 'ACCEPTED', 'ON_THE_WAY', 'ARRIVED', 'IN_PROGRESS'].includes(j.status));
  const completedJobs = jobs.filter(j => j.status === 'COMPLETED');
  const totalSpent = completedJobs.reduce((acc, curr) => acc + (curr.finalPrice || curr.estimatedPrice || 0), 0);

  return (
    <main className="dashboard">
      <div className="dash-top">
        <div>
          <span className="eyebrow">CUSTOMER PORTAL</span>
          <h1>Welcome back, {auth?.name || 'Customer'} <span>👋</span></h1>
          <p>Manage your bookings and discover verified professionals.</p>
        </div>
        <button className="role-switch" onClick={() => setRole('worker')}>View worker portal →</button>
      </div>

      <div className="dash-search" onClick={() => setPage('find')}>
        ⌕ <span>Search for a service or professional</span><b>→</b>
      </div>

      <div className="stat-row">
        <Stat num={activeJobs.length.toString()} label="Active bookings" />
        <Stat num={completedJobs.length.toString()} label="Completed jobs" />
        <Stat num={`₹${totalSpent.toLocaleString()}`} label="Total spent" />
        <Stat num="4.9 ★" label="Avg. rating given" />
      </div>

      <section className="dash-section">
        <div className="section-heading">
          <div>
            <h2>My bookings ({jobs.length})</h2>
            <p>Live status of your requested and in-progress jobs.</p>
          </div>
          <button className="text-button" onClick={loadJobs}>Refresh bookings ↻</button>
        </div>

        {loading ? (
          <div className="empty"><strong>Loading bookings…</strong></div>
        ) : jobs.length === 0 ? (
          <div className="empty">
            <strong>No bookings yet</strong>
            <span>Search for a professional and request a service!</span>
            <button className="primary" style={{ alignSelf: 'center', marginTop: '12px' }} onClick={() => setPage('find')}>
              Find professionals →
            </button>
          </div>
        ) : (
          jobs.map(job => (
            <div className="active-job" key={job.id}>
              <div className="job-icon">{serviceIcons[job.serviceName] || '🔧'}</div>
              <div className="job-main">
                <span className={`status-badge ${job.status}`}>
                  ● {job.status.replace(/_/g, ' ')}
                </span>
                <h3>{job.title} ({job.serviceName})</h3>
                <p>{job.address} · Est: ₹{job.estimatedPrice || 500}</p>
                <small className="muted">{job.description}</small>
              </div>
              <div className="job-person">
                <div className="avatar" style={{ background: '#195a9b' }}>{job.workerName ? job.workerName.slice(0, 2).toUpperCase() : 'WS'}</div>
                <span>
                  <b>{job.workerName || 'Assigned Professional'}</b>
                  <small>{job.scheduledTime || 'Scheduled'}</small>
                </span>
              </div>
              <div className="job-actions">
                {['REQUESTED', 'ACCEPTED'].includes(job.status) && (
                  <button className="outline-button" style={{ color: '#b53939', borderColor: '#b53939' }} onClick={() => handleCancel(job.id)}>
                    Cancel
                  </button>
                )}
              </div>
            </div>
          ))
        )}
      </section>

      <section className="dash-section">
        <div className="section-heading">
          <div>
            <h2>Recommended for you</h2>
            <p>Based on what people in your area are booking.</p>
          </div>
          <button className="text-button" onClick={() => setPage('find')}>View all →</button>
        </div>
        <div className="recommend-grid">
          {workerList.slice(0, 3).map(w => (
            <div className="recommend-card" key={w.id}>
              <div className="recommend-top">
                <Avatar worker={w} />
                <span className="availability">● {w.available ? 'Available' : 'Booked'}</span>
              </div>
              <h3>{w.name} <span className="verified-mini">✓</span></h3>
              <p>{w.role}</p>
              <Stars value={w.rating} /> <span className="muted"> ({w.reviews})</span>
              <div className="recommend-bottom">
                <b>₹{w.rate}<small>/day</small></b>
                <button onClick={() => { setSelected(w); setPage('profile'); }}>View profile →</button>
              </div>
            </div>
          ))}
        </div>
      </section>
    </main>
  );
}

export function WorkerDashboard({ auth, setRole }) {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState('');

  const loadWorkerJobs = () => {
    setLoading(true);
    api.workerJobs()
      .then(data => setJobs(data || []))
      .catch(err => setMsg(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadWorkerJobs();
  }, []);

  const handleAction = async (actionFn, jobId) => {
    try {
      await actionFn(jobId);
      loadWorkerJobs();
    } catch (e) {
      alert(e.message);
    }
  };

  const pendingRequests = jobs.filter(j => j.status === 'REQUESTED');
  const activeJobs = jobs.filter(j => ['ACCEPTED', 'ON_THE_WAY', 'ARRIVED', 'IN_PROGRESS'].includes(j.status));
  const completedJobs = jobs.filter(j => j.status === 'COMPLETED');
  const todayEarnings = completedJobs.reduce((acc, curr) => acc + (curr.finalPrice || curr.estimatedPrice || 0), 0);

  return (
    <main className="dashboard worker-dash">
      <div className="dash-top">
        <div>
          <span className="eyebrow">WORKER WORKSPACE</span>
          <h1>Welcome, {auth?.name || 'Raj Kumar'} <span>👋</span></h1>
          <p>Manage incoming requests and update your active jobs.</p>
        </div>
        <button className="availability-toggle">● You’re Available</button>
      </div>

      <div className="stat-row">
        <Stat num={pendingRequests.length.toString()} label="Pending requests" />
        <Stat num={activeJobs.length.toString()} label="Active jobs" />
        <Stat num={`₹${todayEarnings.toLocaleString()}`} label="Earnings" />
        <Stat num={completedJobs.length.toString()} label="Completed jobs" />
      </div>

      <section className="dash-section">
        <div className="section-heading">
          <div>
            <h2>Service Requests ({pendingRequests.length} pending)</h2>
            <p>Respond to customer requests to secure bookings.</p>
          </div>
          <button className="text-button" onClick={loadWorkerJobs}>Refresh ↻</button>
        </div>

        {pendingRequests.length === 0 ? (
          <div className="empty">
            <strong>No new pending requests</strong>
            <span>You are all caught up! New requests will appear here.</span>
          </div>
        ) : (
          <div className="request-list">
            {pendingRequests.map(r => (
              <article key={r.id}>
                <div className="job-icon">{serviceIcons[r.serviceName] || '⚡'}</div>
                <div>
                  <h3>{r.title} ({r.serviceName})</h3>
                  <p>{r.customerName} · {r.address}</p>
                  <small>“{r.description}”</small>
                </div>
                <strong>₹{r.estimatedPrice || 500}<small> est.</small></strong>
                <div className="request-actions">
                  <button className="outline-button" onClick={() => handleAction(api.rejectJob, r.id)}>Decline</button>
                  <button className="primary" onClick={() => handleAction(api.acceptJob, r.id)}>Accept</button>
                </div>
              </article>
            ))}
          </div>
        )}
      </section>

      <section className="dash-section">
        <div className="section-heading">
          <div>
            <h2>Active & In-Progress Jobs ({activeJobs.length})</h2>
            <p>Update your status as you travel and complete jobs.</p>
          </div>
        </div>

        {activeJobs.length === 0 ? (
          <div className="empty"><strong>No jobs in progress currently</strong></div>
        ) : (
          activeJobs.map(job => (
            <div className="active-job" key={job.id}>
              <div className="job-icon">{serviceIcons[job.serviceName] || '🔧'}</div>
              <div className="job-main">
                <span className={`status-badge ${job.status}`}>● {job.status.replace(/_/g, ' ')}</span>
                <h3>{job.title} · Customer: {job.customerName}</h3>
                <p>{job.address}</p>
                <small className="muted">{job.description}</small>
              </div>
              <div className="job-actions">
                {job.status === 'ACCEPTED' && (
                  <button className="primary" onClick={() => handleAction(api.onTheWay, job.id)}>On The Way 🚗</button>
                )}
                {job.status === 'ON_THE_WAY' && (
                  <button className="primary" onClick={() => handleAction(api.arrived, job.id)}>Mark Arrived 📍</button>
                )}
                {job.status === 'ARRIVED' && (
                  <button className="primary" onClick={() => handleAction(api.startJob, job.id)}>Start Job ⚙️</button>
                )}
                {job.status === 'IN_PROGRESS' && (
                  <button className="primary" onClick={() => handleAction(api.completeJob, job.id)}>Complete Job ✅</button>
                )}
              </div>
            </div>
          ))
        )}
      </section>
    </main>
  );
}

export function AdminDashboard({ auth }) {
  const [stats, setStats] = useState({ totalUsers: 6, totalWorkers: 4, verifiedWorkers: 4, pendingVerifications: 0 });
  const [pendingWorkers, setPendingWorkers] = useState([]);
  const [loading, setLoading] = useState(false);

  const loadAdminData = () => {
    setLoading(true);
    Promise.all([
      api.adminDashboard().catch(() => ({ totalUsers: 6, totalWorkers: 4, verifiedWorkers: 4, pendingVerifications: 0 })),
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
      loadAdminData();
    } catch (e) {
      alert(e.message);
    }
  };

  return (
    <main className="dashboard admin">
      <div className="dash-top">
        <div>
          <span className="eyebrow">PLATFORM OVERVIEW</span>
          <h1>Admin Control Panel</h1>
          <p>Real-time ecosystem statistics and worker verification queue.</p>
        </div>
        <button className="role-switch" onClick={loadAdminData}>Refresh Stats ↻</button>
      </div>

      <div className="stat-row">
        <Stat num={stats.totalUsers?.toString() || '6'} label="Total platform users" />
        <Stat num={stats.totalWorkers?.toString() || '4'} label="Total registered workers" />
        <Stat num={stats.verifiedWorkers?.toString() || '4'} label="Verified pros" />
        <Stat num={stats.pendingVerifications?.toString() || '0'} label="Pending approvals" />
      </div>

      <div className="admin-grid">
        <section className="dash-section panel">
          <h2>Pending Worker Verifications <span className="pill amber">{pendingWorkers.length} pending</span></h2>
          <p>Review worker documents to keep WorkSaathi safe and trusted.</p>

          {pendingWorkers.length === 0 ? (
            <div className="empty" style={{ padding: '30px', marginTop: '12px' }}>
              <strong>All workers are currently verified!</strong>
              <small>Newly registered workers will appear here for identity and document validation.</small>
            </div>
          ) : (
            pendingWorkers.map(w => (
              <div className="verify-row" key={w.id}>
                <div className="avatar" style={{ background: '#0b8a79' }}>{w.name?.slice(0, 2).toUpperCase() || 'W'}</div>
                <span>
                  <b>{w.name}</b>
                  <small>{w.bio || 'New registration'} · Status: {w.verificationStatus}</small>
                </span>
                <button className="primary" style={{ padding: '6px 12px', fontSize: '11px' }} onClick={() => handleVerify(w.id)}>
                  Approve Pro
                </button>
              </div>
            ))
          )}
        </section>

        <section className="dash-section panel">
          <h2>Weekly Platform Activity</h2>
          <p>Completed jobs across Delhi-NCR are up <b className="green">18.4%</b> this month.</p>
          <div className="bar-chart">
            {[45, 62, 58, 85, 76, 95, 82].map((h, i) => (
              <div key={i}>
                <i style={{ height: h + '%' }}></i>
                <span>{['M', 'T', 'W', 'T', 'F', 'S', 'S'][i]}</span>
              </div>
            ))}
          </div>
        </section>
      </div>
    </main>
  );
}

export function Stat({ num, label }) {
  return (
    <div className="stat">
      <strong>{num}</strong>
      <span>{label}</span>
    </div>
  );
}

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
  };

  const field = (key, label, type = 'text') => (
    <label>
      {label}
      <input
        required
        type={type}
        value={form[key]}
        onChange={e => setForm({ ...form, [key]: e.target.value })}
      />
    </label>
  );

  return (
    <main className="auth-page">
      <form className="auth-card" onSubmit={submit}>
        <div className="brand"><i>W</i> Work<span>Saathi</span></div>
        <div className="eyebrow">ACCOUNT ACCESS</div>
        <h1>{mode === 'login' ? 'Welcome back' : 'Create your account'}</h1>
        <p>{mode === 'login' ? 'Log in to manage your services and bookings.' : 'Join to find or offer trusted local services.'}</p>

        {mode === 'register' && (
          <>
            {field('name', 'Full name')}
            {field('phone', 'Phone number')}
          </>
        )}

        {field('email', 'Email address', 'email')}
        {field('password', 'Password', 'password')}

        {mode === 'register' && (
          <label>
            I want to join as
            <select value={form.role} onChange={e => setForm({ ...form, role: e.target.value })}>
              <option value="CUSTOMER">Customer (Book Services)</option>
              <option value="WORKER">Worker (Provide Services)</option>
            </select>
          </label>
        )}

        {error && <p className="form-error">{error}</p>}

        <button className="primary wide" disabled={busy}>
          {busy ? 'Please wait…' : mode === 'login' ? 'Log in' : 'Create account'} <b>→</b>
        </button>

        <button type="button" className="auth-switch" onClick={() => setMode(mode === 'login' ? 'register' : 'login')}>
          {mode === 'login' ? 'New to WorkSaathi? Create an account' : 'Already have an account? Log in'}
        </button>

        <div className="demo-accounts">
          <small>Quick 1-Click Demo Accounts</small>
          <div className="demo-grid">
            <button type="button" className="demo-btn" onClick={() => fillDemo('customer@worksaathi.com', 'customer')}>
              👨 Customer
            </button>
            <button type="button" className="demo-btn" onClick={() => fillDemo('raj@worksaathi.com', 'worker')}>
              ⚡ Worker
            </button>
            <button type="button" className="demo-btn" onClick={() => fillDemo('admin@worksaathi.com', 'admin')}>
              🛡️ Admin
            </button>
          </div>
        </div>
      </form>
    </main>
  );
}

export function Footer() {
  return (
    <footer className="footer">
      <div className="brand"><i>W</i> Work<span>Saathi</span></div>
      <p>India's trusted platform for skilled local home & commercial service professionals.</p>
      <small>© 2026 WorkSaathi Platform. All rights reserved.</small>
    </footer>
  );
}

export function mapWorker(worker, index = 0) {
  const palette = ['#195a9b', '#0b8a79', '#a15c26', '#944e78'];
  const names = (worker?.name || 'Local Professional').split(' ');
  const initials = names.map(n => n[0]).join('').slice(0, 2).toUpperCase();

  return {
    id: worker.id,
    name: worker.name || 'Local Professional',
    role: worker.services?.[0] || 'Verified Professional',
    rating: worker.averageRating || 4.8,
    reviews: worker.totalReviews || 12,
    rate: worker.dailyRate || worker.hourlyRate || 450,
    distance: worker.distance || (1.5 + (index * 0.6)),
    exp: worker.experienceYears || 4,
    bio: worker.bio,
    photo: initials,
    color: palette[index % palette.length],
    available: worker.availabilityStatus === 'AVAILABLE' || worker.availabilityStatus === 'APPROVED',
    skills: worker.services?.length ? worker.services : ['General Maintenance']
  };
}

export function App() {
  const [page, setPage] = useState('home');
  const [role, setRole] = useState('customer');
  const [query, setQuery] = useState('');
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
  };

  const workerList = liveWorkers.length ? liveWorkers : fallbackWorkers;

  return (
    <>
      <Header
        page={page}
        setPage={setPage}
        role={role}
        setRole={setRole}
        auth={auth}
        onLogout={logout}
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
