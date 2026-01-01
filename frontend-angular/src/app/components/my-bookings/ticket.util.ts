import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { Reservation } from '../../models/reservation.model';
import { Stadium } from '../../models/stadium.model';

export function generateTicketPDF(reservation: Reservation, stadium: Stadium) {
  const doc = new jsPDF();
  doc.setFontSize(22);
  doc.text('Reservation Ticket', 105, 20, { align: 'center' });
  doc.setFontSize(14);
  doc.text(`Stadium: ${stadium.name}`, 20, 40);
  doc.text(`Location: ${stadium.location}`, 20, 50);
  doc.text(`Date: ${reservation.date}`, 20, 60);
  doc.text(`Time: ${reservation.heureDebut} - ${reservation.heureFin}`, 20, 70);
  doc.text(`Status: ${reservation.statut}`, 20, 80);
  doc.text(`Price paid: ${reservation.prix ?? '-'} DH`, 20, 90);
  doc.setDrawColor(22, 160, 133);
  doc.setLineWidth(1.5);
  doc.line(20, 95, 190, 95);
  doc.setFontSize(12);
  doc.text('Thank you for booking with FootReserve!', 105, 110, { align: 'center' });
  doc.save(`ticket_reservation_${reservation.id || ''}.pdf`);
}
