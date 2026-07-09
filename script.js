const state = {
  selectedDate: new Date(),
  currentMonth: new Date().getMonth(),
  currentYear: new Date().getFullYear(),
  holidays: new Set(),
  patternShiftCount: 0,
  patternLabel: 'Odd Pattern',
  activePoles: [],
  inactivePoles: []
};

const holidayApiUrl = 'https://date.nager.at/api/v3/PublicHolidays/2026/IN';

function formatDate(date) {
  return new Date(date.getFullYear(), date.getMonth(), date.getDate()).toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' });
}

function formatDayName(date) {
  return date.toLocaleDateString('en-US', { weekday: 'long' });
}

function isHoliday(date) {
  const key = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
  return state.holidays.has(key);
}

function evaluatePattern(date) {
  const isHolidayDate = isHoliday(date);
  const day = date.getDate();
  let pattern = day % 2 !== 0 ? 'Odd Pattern' : 'Even Pattern';
  if (isHolidayDate) {
    state.patternLabel = 'Government Holiday';
    state.activePoles = [];
    state.inactivePoles = [1, 2, 3, 4, 5, 6, 7];
    return { isHoliday: true, patternLabel: 'Government Holiday' };
  }
  if (state.patternShiftCount % 2 === 1) {
    pattern = pattern === 'Odd Pattern' ? 'Even Pattern' : 'Odd Pattern';
  }
  state.patternLabel = pattern;
  state.activePoles = pattern === 'Odd Pattern' ? [1, 3, 5, 7] : [2, 4, 6];
  state.inactivePoles = pattern === 'Odd Pattern' ? [2, 4, 6] : [1, 3, 5, 7];
  return { isHoliday: false, patternLabel: pattern };
}

function getHolidayInsights(date) {
  const currentYear = date.getFullYear();
  const holidays = [];
  for (let y = currentYear - 1; y <= currentYear + 1; y += 1) {
    holidays.push(...[new Date(y, 0, 1), new Date(y, 0, 26), new Date(y, 4, 1), new Date(y, 7, 15), new Date(y, 9, 2), new Date(y, 11, 25)]);
  }
  const future = holidays.filter(item => item > date && item.getDay() !== 0).sort((a, b) => a - b);
  const past = holidays.filter(item => item <= date && item.getDay() !== 0).sort((a, b) => b - a);
  return { prev: past[0], next: future[0] };
}

function render() {
  document.getElementById('selectedDate').textContent = formatDate(state.selectedDate);
  document.getElementById('dayName').textContent = formatDayName(state.selectedDate);
  document.getElementById('holidayStatus').textContent = isHoliday(state.selectedDate) ? 'Yes' : 'No';
  document.getElementById('patternLabel').textContent = state.patternLabel;
  document.getElementById('patternShiftCount').textContent = state.patternShiftCount;
  document.getElementById('lightsOn').textContent = state.activePoles.length;
  document.getElementById('lightsOff').textContent = state.inactivePoles.length;
  document.getElementById('energySaved').textContent = `${Math.round((state.inactivePoles.length / 7) * 100)}%`;
  document.getElementById('systemStatus').textContent = isHoliday(state.selectedDate) ? 'Holiday - All Lights OFF' : 'Simulation Complete';
  document.getElementById('activePoles').textContent = `Active Poles : ${state.activePoles.join(', ') || 'None'}`;
  document.getElementById('inactivePoles').textContent = `Inactive Poles : ${state.inactivePoles.join(', ') || 'None'}`;
  const insights = getHolidayInsights(state.selectedDate);
  document.getElementById('prevHoliday').textContent = insights.prev ? formatDate(insights.prev) : 'None';
  document.getElementById('nextHoliday').textContent = insights.next ? formatDate(insights.next) : 'None';
  document.getElementById('daysRemaining').textContent = insights.next ? `${Math.round((insights.next - state.selectedDate) / 86400000)} day(s)` : '--';
  document.getElementById('dateInput').value = state.selectedDate.toISOString().slice(0, 10);
  document.getElementById('currentDate').textContent = `Today: ${formatDate(new Date())}`;
  document.getElementById('currentTime').textContent = `Time: ${new Date().toLocaleTimeString()}`;
  renderCalendar();
}

function renderCalendar() {
  const grid = document.getElementById('calendarGrid');
  grid.innerHTML = '';
  const monthTitle = document.getElementById('calendarTitle');
  const monthDate = new Date(state.currentYear, state.currentMonth, 1);
  monthTitle.textContent = monthDate.toLocaleDateString('en-US', { month: 'long', year: 'numeric' });
  const firstDay = new Date(state.currentYear, state.currentMonth, 1).getDay();
  const daysInMonth = new Date(state.currentYear, state.currentMonth + 1, 0).getDate();
  const labels = ['Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa', 'Su'];
  labels.forEach(label => {
    const el = document.createElement('div');
    el.textContent = label;
    el.className = 'calendar-label';
    grid.appendChild(el);
  });
  for (let i = 1; i < firstDay; i += 1) {
    grid.appendChild(document.createElement('div'));
  }
  for (let day = 1; day <= daysInMonth; day += 1) {
    const cell = document.createElement('button');
    const date = new Date(state.currentYear, state.currentMonth, day);
    cell.textContent = day;
    const classes = [];
    if (isHoliday(date)) classes.push('holiday');
    else if (day % 2 === 0) classes.push('even');
    else classes.push('odd');
    if (date.toDateString() === state.selectedDate.toDateString()) classes.push('selected');
    if (date.toDateString() === new Date().toDateString()) classes.push('today');
    cell.className = classes.join(' ');
    cell.addEventListener('click', () => {
      state.selectedDate = date;
      render();
    });
    grid.appendChild(cell);
  }
}

function simulate() {
  const result = evaluatePattern(state.selectedDate);
  state.patternShiftCount += result.isHoliday ? 1 : 0;
  render();
}

function reset() {
  state.selectedDate = new Date();
  state.currentMonth = new Date().getMonth();
  state.currentYear = new Date().getFullYear();
  state.patternShiftCount = 0;
  render();
}

document.getElementById('simulateBtn').addEventListener('click', simulate);
document.getElementById('resetBtn').addEventListener('click', reset);
document.getElementById('dateInput').addEventListener('change', (event) => {
  const [year, month, day] = event.target.value.split('-').map(Number);
  state.selectedDate = new Date(year, month - 1, day);
  render();
});
document.getElementById('prevMonth').addEventListener('click', () => {
  state.currentMonth -= 1;
  if (state.currentMonth < 0) { state.currentMonth = 11; state.currentYear -= 1; }
  renderCalendar();
});
document.getElementById('nextMonth').addEventListener('click', () => {
  state.currentMonth += 1;
  if (state.currentMonth > 11) { state.currentMonth = 0; state.currentYear += 1; }
  renderCalendar();
});

fetch(holidayApiUrl)
  .then(response => response.json())
  .then(data => {
    data.forEach(item => state.holidays.add(item.date));
    render();
  })
  .catch(() => render());

setInterval(() => {
  document.getElementById('currentTime').textContent = `Time: ${new Date().toLocaleTimeString()}`;
}, 1000);

render();
