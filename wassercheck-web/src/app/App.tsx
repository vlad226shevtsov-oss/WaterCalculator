import { useState } from "react";
import {
  Bath,
  Coins,
  Droplets,
  Gauge,
  Globe2,
  Leaf,
  Lightbulb,
  Minus,
  Plus,
  ShowerHead,
  Sparkles,
  Zap,
} from "lucide-react";

type Language = "de" | "ru";
type Currency = "EUR" | "USD" | "UAH";
type Mode = "shower" | "bath";

const RATES: Record<Currency, number> = { EUR: 1, USD: 1.1467, UAH: 51.4631 };
const CURRENCY_LOCALE: Record<Language, string> = { de: "de-DE", ru: "ru-RU" };

const copy = {
  de: {
    subtitle: "Wasser- & Energieverbrauch im Blick",
    title: "Verbrauchsrechner",
    intro: "Berechne Verbrauch, Kosten und Einsparpotenzial — direkt im Browser.",
    live: "Live-Berechnung",
    settings: "Einstellungen",
    language: "Sprache",
    currency: "Währung",
    choose: "Was nutzt du?",
    shower: "Dusche",
    showerHint: "Fließendes Wasser · ca. 12 L/min",
    bath: "Bad",
    bathHint: "Volle Badewanne · ca. 120 Liter",
    duration: "Wie viele Minuten duschst du pro Tag?",
    durationHint: "Stelle deine durchschnittliche Duschzeit ein",
    minutes: "Minuten",
    results: "Dein Ergebnis pro Nutzung",
    water: "Wasser",
    energy: "Energie",
    total: "Gesamtkosten",
    waterCost: "Wasserkosten",
    energyCost: "Energiekosten",
    month: "Monat",
    year: "Jahr",
    forecast: "Kostenprognose",
    rating: "Bewertung",
    veryGood: "Sehr gut",
    good: "Gut",
    normal: "Normal",
    high: "Hoch",
    tip: "Spartipp",
    tipText: "Schon 3 Minuten weniger Duschen sparen bei 12 L/min täglich 36 Liter Wasser.",
    footprint: "Umweltwirkung pro Monat",
    saved: "Mögliches Sparpotenzial",
    savedHint: "gegenüber deiner Zeit, wenn du auf 5 Minuten reduzierst",
    source: "Richtwerte: 12 L/min, 30 °C Erwärmung, 2,50 €/m³ Wasser, 0,40 €/kWh.",
    exchange: "Umrechnung auf Basis hinterlegter Referenzkurse.",
  },
  ru: {
    subtitle: "Контроль воды и энергии",
    title: "Калькулятор расхода",
    intro: "Рассчитайте расход, стоимость и потенциал экономии прямо в браузере.",
    live: "Расчёт в реальном времени",
    settings: "Настройки",
    language: "Язык",
    currency: "Валюта",
    choose: "Что вы используете?",
    shower: "Душ",
    showerHint: "Проточная вода · около 12 л/мин",
    bath: "Ванна",
    bathHint: "Полная ванна · около 120 литров",
    duration: "Сколько минут в день вы принимаете душ?",
    durationHint: "Укажите среднюю продолжительность",
    minutes: "минут",
    results: "Результат за одно использование",
    water: "Вода",
    energy: "Энергия",
    total: "Общая стоимость",
    waterCost: "Стоимость воды",
    energyCost: "Стоимость энергии",
    month: "Месяц",
    year: "Год",
    forecast: "Прогноз расходов",
    rating: "Оценка",
    veryGood: "Отлично",
    good: "Хорошо",
    normal: "Нормально",
    high: "Высокий расход",
    tip: "Совет",
    tipText: "Если сократить душ на 3 минуты, при 12 л/мин можно экономить 36 литров воды ежедневно.",
    footprint: "Воздействие за месяц",
    saved: "Возможная экономия",
    savedHint: "если сократить продолжительность душа до 5 минут",
    source: "Расчётные значения: 12 л/мин, нагрев на 30 °C, вода 2,50 €/м³, энергия 0,40 €/кВт·ч.",
    exchange: "Пересчёт выполнен по сохранённым справочным курсам.",
  },
} as const;

function Card({ children, className = "" }: { children: React.ReactNode; className?: string }) {
  return (
    <section className={`rounded-3xl border border-white/[0.09] bg-white/[0.045] shadow-[inset_0_1px_0_rgba(255,255,255,.06)] backdrop-blur-2xl ${className}`}>
      {children}
    </section>
  );
}

function Metric({ icon, color, label, value, progress }: { icon: React.ReactNode; color: string; label: string; value: string; progress: number }) {
  return (
    <Card className="p-5">
      <div className="mb-5 flex items-center justify-between">
        <span className="rounded-xl p-2.5" style={{ color, background: `${color}18` }}>{icon}</span>
        <span className="text-xs text-white/35">{Math.round(progress)}%</span>
      </div>
      <p className="text-sm text-white/45">{label}</p>
      <p className="mt-1 text-2xl font-bold text-white" style={{ fontFamily: "Outfit, sans-serif" }}>{value}</p>
      <div className="mt-4 h-1.5 overflow-hidden rounded-full bg-white/[0.07]">
        <div className="h-full rounded-full transition-all duration-500" style={{ width: `${Math.min(progress, 100)}%`, background: color, boxShadow: `0 0 12px ${color}` }} />
      </div>
    </Card>
  );
}

export default function App() {
  const [language, setLanguage] = useState<Language>("de");
  const [currency, setCurrency] = useState<Currency>("EUR");
  const [mode, setMode] = useState<Mode>("shower");
  const [minutes, setMinutes] = useState(8);
  const t = copy[language];

  const liters = mode === "shower" ? minutes * 12 : 120;
  const kwh = liters * 0.001163 * 30;
  const waterEur = (liters / 1000) * 2.5;
  const energyEur = kwh * 0.4;
  const totalEur = waterEur + energyEur;
  const converted = (eur: number) => eur * RATES[currency];
  const money = (eur: number) => new Intl.NumberFormat(CURRENCY_LOCALE[language], { style: "currency", currency, maximumFractionDigits: 2 }).format(converted(eur));
  const number = (value: number, digits = 1) => new Intl.NumberFormat(CURRENCY_LOCALE[language], { maximumFractionDigits: digits }).format(value);
  const rating = liters <= 60 ? t.veryGood : liters <= 90 ? t.good : liters <= 130 ? t.normal : t.high;
  const ratingColor = liters <= 90 ? "#22c55e" : liters <= 130 ? "#facc15" : "#ef4444";
  const savedLiters = mode === "shower" ? Math.max(0, minutes - 5) * 12 : 0;

  return (
    <div lang={language} className="relative min-h-screen overflow-hidden bg-[#020617] text-white" style={{ fontFamily: "Inter, sans-serif" }}>
      <div className="pointer-events-none fixed inset-0 bg-[radial-gradient(circle_at_75%_10%,rgba(14,165,233,.16),transparent_35%),radial-gradient(circle_at_15%_80%,rgba(34,197,94,.08),transparent_30%),linear-gradient(135deg,#020617,#071a2e_50%,#0b2447)]" />
      <div className="pointer-events-none fixed inset-0 opacity-[.025] [background-image:linear-gradient(rgba(14,165,233,.8)_1px,transparent_1px),linear-gradient(90deg,rgba(14,165,233,.8)_1px,transparent_1px)] [background-size:72px_72px]" />

      <header className="relative z-10 border-b border-white/[0.07] bg-black/20 backdrop-blur-2xl">
        <div className="mx-auto flex max-w-6xl flex-wrap items-center justify-between gap-4 px-4 py-4 sm:px-8">
          <div className="flex items-center gap-3">
            <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-gradient-to-br from-sky-400 to-sky-700 shadow-[0_0_24px_rgba(14,165,233,.35)]"><Droplets size={20} /></div>
            <div><p className="text-xl font-bold" style={{ fontFamily: "Outfit, sans-serif" }}>Wasser<span className="text-sky-400">Check</span></p><p className="text-[10px] uppercase tracking-[.16em] text-white/35">{t.subtitle}</p></div>
          </div>
          <div className="flex items-center gap-2" aria-label={t.settings}>
            <Globe2 size={16} className="hidden text-white/35 sm:block" />
            <label className="sr-only" htmlFor="language">{t.language}</label>
            <select id="language" value={language} onChange={(e) => setLanguage(e.target.value as Language)} className="rounded-xl border border-white/10 bg-white/[0.06] px-3 py-2 text-sm outline-none focus:border-sky-400"><option className="bg-slate-900" value="de">DE</option><option className="bg-slate-900" value="ru">RU</option></select>
            <label className="sr-only" htmlFor="currency">{t.currency}</label>
            <select id="currency" value={currency} onChange={(e) => setCurrency(e.target.value as Currency)} className="rounded-xl border border-white/10 bg-white/[0.06] px-3 py-2 text-sm outline-none focus:border-sky-400">{Object.keys(RATES).map((item) => <option className="bg-slate-900" key={item}>{item}</option>)}</select>
          </div>
        </div>
      </header>

      <main className="relative z-10 mx-auto max-w-6xl space-y-6 px-4 py-8 sm:px-8">
        <div className="flex flex-wrap items-end justify-between gap-4">
          <div><h1 className="text-3xl font-bold sm:text-4xl" style={{ fontFamily: "Outfit, sans-serif" }}>{t.title}</h1><p className="mt-2 max-w-2xl text-sm text-white/45 sm:text-base">{t.intro}</p></div>
          <span className="flex items-center gap-2 rounded-2xl border border-emerald-500/20 bg-emerald-500/[0.08] px-4 py-2 text-xs text-emerald-300"><span className="h-2 w-2 animate-pulse rounded-full bg-emerald-400" />{t.live}</span>
        </div>

        <Card className="p-5 sm:p-7">
          <h2 className="mb-4 text-lg font-semibold">{t.choose}</h2>
          <div className="grid gap-4 sm:grid-cols-2">
            {([
              { id: "shower" as Mode, icon: ShowerHead, title: t.shower, hint: t.showerHint },
              { id: "bath" as Mode, icon: Bath, title: t.bath, hint: t.bathHint },
            ]).map(({ id, icon: Icon, title, hint }) => (
              <button key={id} onClick={() => setMode(id)} className={`flex items-center gap-4 rounded-2xl border p-5 text-left transition ${mode === id ? "border-sky-400/50 bg-sky-400/[0.09] shadow-[0_0_35px_rgba(14,165,233,.1)]" : "border-white/[0.08] bg-white/[0.025] hover:bg-white/[0.06]"}`}>
                <span className={`rounded-2xl p-3 ${mode === id ? "bg-sky-400 text-white" : "bg-white/[0.06] text-white/45"}`}><Icon size={25} /></span>
                <span><strong className="block text-lg">{title}</strong><small className="text-white/40">{hint}</small></span>
              </button>
            ))}
          </div>
        </Card>

        {mode === "shower" && <Card className="p-5 sm:p-7">
          <h2 className="text-lg font-semibold">{t.duration}</h2><p className="mt-1 text-sm text-white/40">{t.durationHint}</p>
          <div className="mt-7 flex items-center justify-center gap-6">
            <button aria-label="-1" onClick={() => setMinutes((v) => Math.max(1, v - 1))} className="rounded-2xl border border-white/10 bg-white/[0.05] p-4 hover:bg-white/10"><Minus /></button>
            <div className="min-w-24 text-center"><strong className="block text-5xl" style={{ fontFamily: "Outfit, sans-serif" }}>{minutes}</strong><span className="text-xs text-white/40">{t.minutes}</span></div>
            <button aria-label="+1" onClick={() => setMinutes((v) => Math.min(60, v + 1))} className="rounded-2xl border border-white/10 bg-white/[0.05] p-4 hover:bg-white/10"><Plus /></button>
          </div>
          <input aria-label={t.duration} className="mt-7 w-full accent-sky-500" type="range" min="1" max="60" value={minutes} onChange={(e) => setMinutes(Number(e.target.value))} />
          <div className="mt-2 flex justify-between text-xs text-white/25"><span>1</span><span>30</span><span>60</span></div>
        </Card>}

        <div>
          <h2 className="mb-4 text-lg font-semibold">{t.results}</h2>
          <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
            <Metric icon={<Droplets size={20} />} color="#0ea5e9" label={t.water} value={`${number(liters, 0)} L`} progress={(liters / 150) * 100} />
            <Metric icon={<Zap size={20} />} color="#facc15" label={t.energy} value={`${number(kwh, 2)} kWh`} progress={(kwh / 6) * 100} />
            <Metric icon={<Coins size={20} />} color="#a78bfa" label={t.total} value={money(totalEur)} progress={(totalEur / 3) * 100} />
            <Metric icon={<Gauge size={20} />} color={ratingColor} label={t.rating} value={rating} progress={Math.max(10, 100 - (liters / 160) * 100)} />
          </div>
        </div>

        <div className="grid gap-6 lg:grid-cols-2">
          <Card className="p-6">
            <h2 className="flex items-center gap-2 text-lg font-semibold"><Coins className="text-violet-400" size={20} />{t.forecast}</h2>
            <div className="mt-5 space-y-3 text-sm">
              <div className="flex justify-between text-white/55"><span>{t.waterCost}</span><strong className="text-white">{money(waterEur)}</strong></div>
              <div className="flex justify-between text-white/55"><span>{t.energyCost}</span><strong className="text-white">{money(energyEur)}</strong></div>
              <div className="h-px bg-white/[0.08]" />
              <div className="flex justify-between"><span>{t.month}</span><strong className="text-xl text-sky-300">{money(totalEur * 30)}</strong></div>
              <div className="flex justify-between"><span>{t.year}</span><strong>{money(totalEur * 365)}</strong></div>
            </div>
          </Card>
          <Card className="p-6">
            <h2 className="flex items-center gap-2 text-lg font-semibold"><Leaf className="text-emerald-400" size={20} />{t.footprint}</h2>
            <div className="mt-5 grid grid-cols-2 gap-4">
              <div className="rounded-2xl bg-sky-400/[0.08] p-4"><p className="text-xs text-white/40">{t.water}</p><strong className="mt-1 block text-2xl text-sky-300">{number(liters * 30, 0)} L</strong></div>
              <div className="rounded-2xl bg-emerald-400/[0.08] p-4"><p className="text-xs text-white/40">CO₂</p><strong className="mt-1 block text-2xl text-emerald-300">{number(kwh * 30 * 0.38, 1)} kg</strong></div>
            </div>
          </Card>
        </div>

        <Card className="border-sky-400/20 p-6">
          <div className="flex gap-4"><div className="h-fit rounded-2xl bg-sky-400/15 p-3 text-sky-300"><Lightbulb /></div><div><h2 className="font-semibold">{t.tip}</h2><p className="mt-1 text-sm leading-6 text-white/50">{t.tipText}</p>{savedLiters > 0 && <p className="mt-3 flex items-center gap-2 text-sm text-emerald-300"><Sparkles size={16} /><strong>{t.saved}: {number(savedLiters * 30, 0)} L / {t.month.toLowerCase()}</strong> — {t.savedHint}</p>}</div></div>
        </Card>

        <footer className="pb-5 text-center text-xs leading-5 text-white/25"><p>{t.source}</p>{currency !== "EUR" && <p>{t.exchange} 1 EUR = {RATES[currency]} {currency}</p>}<p className="mt-2">WasserCheck · 2026</p></footer>
      </main>
    </div>
  );
}
